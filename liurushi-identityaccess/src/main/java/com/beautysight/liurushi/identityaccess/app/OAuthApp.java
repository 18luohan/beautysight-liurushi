/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.app;

import com.beautysight.liurushi.common.ex.AuthException;
import com.beautysight.liurushi.common.ex.IllegalParamException;
import com.beautysight.liurushi.identityaccess.app.cache.AccessTokenCache;
import com.beautysight.liurushi.identityaccess.app.command.AuthCommand;
import com.beautysight.liurushi.identityaccess.app.command.DeviceDTO;
import com.beautysight.liurushi.identityaccess.app.command.RefreshAccessTokenCommand;
import com.beautysight.liurushi.identityaccess.app.presentation.AccessTokenPresentation;
import com.beautysight.liurushi.identityaccess.common.AuthErrorId;
import com.beautysight.liurushi.identityaccess.domain.model.AccessToken;
import com.beautysight.liurushi.identityaccess.domain.model.Device;
import com.beautysight.liurushi.identityaccess.domain.model.User;
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

    public void authenticate(final AuthCommand authCommand) {
        AccessToken theToken = accessTokenCache.getIfAbsentLoad(authCommand, new Callable<AccessToken>() {
            @Override
            public AccessToken call() throws Exception {
                return accessTokenService.checkValidityAndLoad(
                        authCommand.accessToken, authCommand.type);
            }
        });

        if (theToken.isExpired()) {
            accessTokenCache.evictBy(authCommand);
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

        if (theToken.get().isExpired()) {
            return AccessTokenPresentation.from(accessTokenService.refreshBearerToken(theToken.get()));
        }

        return AccessTokenPresentation.from(theToken.get());
    }

    public UserClient getUserClientBy(String type, String accessToken) {
        return accessTokenService.getUserClientBy(AccessToken.Type.valueOf(type), accessToken);
    }

    public User getUserBy(String type, String accessToken) {
        return accessTokenService.getUserBy(type, accessToken);
    }

}
