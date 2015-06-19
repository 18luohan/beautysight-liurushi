/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.app;

import com.beautysight.liurushi.common.ex.CacheException;
import com.beautysight.liurushi.common.utils.GuavaCaches;
import com.beautysight.liurushi.identityaccess.app.command.AccessTokenDTO;
import com.beautysight.liurushi.identityaccess.app.command.DeviceDTO;
import com.beautysight.liurushi.identityaccess.app.presentation.AccessTokenPresentation;
import com.beautysight.liurushi.identityaccess.domain.model.AccessToken;
import com.beautysight.liurushi.identityaccess.domain.model.Device;
import com.beautysight.liurushi.identityaccess.domain.model.UserClient;
import com.beautysight.liurushi.identityaccess.domain.repo.DeviceRepo;
import com.beautysight.liurushi.identityaccess.domain.service.AccessTokenService;
import com.google.common.cache.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

/**
 * @author chenlong
 * @since 1.0
 */
@Service
public class OAuthApp {

    @Autowired
    private DeviceRepo deviceRepo;
    @Autowired
    private AccessTokenService accessTokenService;

//    private Cache<AccessTokenCacheKey, AccessTokenPresentation> accessTokenCache = GuavaCaches.createCache();

    public AccessTokenPresentation getIfAbsentIssueBasicTokenFor(final DeviceDTO deviceDTO) {
//        try {
//            AccessTokenPresentation result = accessTokenCache.get(AccessTokenCacheKey.newBasicTokenCacheKey(deviceDTO.imei),
//                    new Callable<AccessTokenPresentation>() {
//                        @Override
//                        public AccessTokenPresentation call() throws Exception {
//                            return OAuthApp.this.getIfAbsentIssueBasicTokenFor(deviceDTO);
//                        }
//                    });
//            if (result != null) {
//                return result;
//            }
//        } catch (ExecutionException ex) {
//            throw new CacheException("Error cache for AccessToken", ex);
//        }

        Device theDevice = deviceRepo.saveOrGet(deviceDTO.toDevice());
        AccessToken basicToken = accessTokenService.getIfAbsentIssueBasicTokenFor(theDevice);
        return AccessTokenPresentation.from(basicToken);
    }

    @Cacheable(value = "userClientCache", keyGenerator = "userClientCacheKeyGenerator")
    public UserClient authenticate(AccessTokenDTO accessTokenDTO) {
        // TODO 最好直接从缓存中根据access token获取用户端信息。
        // TODO 什么时机以access token为key将用户添加至缓存？
        // TODO 如果access token过期了，什么时机将其从缓存中移除？
        return accessTokenService.authenticate(accessTokenDTO.accessToken, accessTokenDTO.type);
    }

}
