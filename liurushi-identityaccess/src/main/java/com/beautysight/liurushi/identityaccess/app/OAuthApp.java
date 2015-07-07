/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.app;

import com.beautysight.liurushi.common.ex.AuthException;
import com.beautysight.liurushi.common.ex.IllegalParamException;
import com.beautysight.liurushi.identityaccess.app.cache.AccessTokenCache;
import com.beautysight.liurushi.identityaccess.app.command.AccessTokenDTO;
import com.beautysight.liurushi.identityaccess.app.command.DeviceDTO;
import com.beautysight.liurushi.identityaccess.app.command.RefreshAccessTokenCommand;
import com.beautysight.liurushi.identityaccess.app.presentation.AccessTokenPresentation;
import com.beautysight.liurushi.identityaccess.common.AuthErrorId;
import com.beautysight.liurushi.identityaccess.domain.model.AccessToken;
import com.beautysight.liurushi.identityaccess.domain.model.Device;
import com.beautysight.liurushi.identityaccess.domain.model.UserClient;
import com.beautysight.liurushi.identityaccess.domain.repo.AccessTokenRepo;
import com.beautysight.liurushi.identityaccess.domain.repo.DeviceRepo;
import com.beautysight.liurushi.identityaccess.domain.service.AccessTokenService;
import com.google.common.base.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.Callable;

/**
 * @author chenlong
 * @since 1.0
 */
@Service
public class OAuthApp {

    @Autowired
    private DeviceRepo deviceRepo;
    @Autowired
    private AccessTokenRepo accessTokenRepo;

    @Autowired
    private AccessTokenService accessTokenService;

    private AccessTokenCache accessTokenCache = new AccessTokenCache();

    public AccessTokenPresentation getIfAbsentIssueBasicTokenFor(final DeviceDTO deviceDTO) {
        Device theDevice = deviceRepo.saveOrGet(deviceDTO.toDevice());
        AccessToken basicToken = accessTokenService.getIfAbsentIssueBasicTokenFor(theDevice);
        return AccessTokenPresentation.from(basicToken);
    }

    public void authenticate(final AccessTokenDTO accessTokenDTO) {
        AccessToken theToken = accessTokenCache.getIfAbsentLoad(accessTokenDTO, new Callable<AccessToken>() {
            @Override
            public AccessToken call() throws Exception {
                return accessTokenService.checkValidityAndLoad(
                        accessTokenDTO.accessToken, accessTokenDTO.type);
            }
        });

        if (theToken.isExpired()) {
            accessTokenCache.evictBy(accessTokenDTO);
            throw new AuthException(AuthErrorId.expired_access_token, "Expired %s token: %s",
                    theToken.type(),
                    theToken.accessToken());
        }
    }

    public AccessTokenPresentation refreshBearerToken(RefreshAccessTokenCommand command) {
        Optional<AccessToken> theToken = accessTokenRepo.accessTokenOf(
                command.bearerToken, AccessToken.Type.Bearer);
        if (!theToken.isPresent()) {
            throw new AuthException(AuthErrorId.invalid_access_token, "Invalid bearer token: %s", command.bearerToken);
        }

        if (!theToken.get().refreshToken().equals(command.refreshToken)) {
            throw new IllegalParamException("refreshToken invalid");
        }

        return AccessTokenPresentation.from(accessTokenService.refreshBearerToken(theToken.get()));
    }

    public UserClient getUserClientBy(AccessTokenDTO accessTokenDTO) {
        return accessTokenService.getUserClientBy(accessTokenDTO.type, accessTokenDTO.accessToken);
    }

}
