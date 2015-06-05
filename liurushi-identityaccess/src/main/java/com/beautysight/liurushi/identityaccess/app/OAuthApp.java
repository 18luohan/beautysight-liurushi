/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.app;

import com.beautysight.liurushi.identityaccess.app.command.AccessTokenDTO;
import com.beautysight.liurushi.identityaccess.app.command.DeviceDTO;
import com.beautysight.liurushi.identityaccess.app.presentation.AccessTokenPresentation;
import com.beautysight.liurushi.identityaccess.domain.model.AccessToken;
import com.beautysight.liurushi.identityaccess.domain.model.Device;
import com.beautysight.liurushi.identityaccess.domain.model.UserClient;
import com.beautysight.liurushi.identityaccess.domain.repo.DeviceRepo;
import com.beautysight.liurushi.identityaccess.domain.service.AccessTokenService;
import com.google.common.base.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Here is Javadoc.
 * <p/>
 * Created by chenlong on 2015-05-27.
 *
 * @author chenlong
 * @since 1.0
 */
@Service
public class OAuthApp {

    @Autowired
    private DeviceRepo deviceRepo;
    @Autowired
    private AccessTokenService accessTokenService;

    public AccessTokenPresentation issueOrGetBasicTokenFor(DeviceDTO deviceDTO) {
        Device theDevice = deviceRepo.saveOrGet(deviceDTO.toDevice());
        AccessToken basicToken = accessTokenService.issueOrGetBasicTokenFor(theDevice);
        return AccessTokenPresentation.from(basicToken);
    }

    public Optional<UserClient> authenticate(AccessTokenDTO accessTokenDTO) {
        // TODO 最好直接从缓存中根据access token获取用户端信息。
        // TODO 什么时机以access token为key将用户添加至缓存？
        // TODO 如果access token过期了，什么时机将其从缓存中移除？
        accessTokenDTO.validate();
        return accessTokenService.authenticate(accessTokenDTO.accessToken, accessTokenDTO.type);
    }

}
