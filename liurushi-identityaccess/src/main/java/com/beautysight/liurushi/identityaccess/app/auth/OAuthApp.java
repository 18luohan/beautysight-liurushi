/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.app.auth;

import com.beautysight.liurushi.common.ex.IllegalParamException;
import com.beautysight.liurushi.identityaccess.common.AuthErrorId;
import com.beautysight.liurushi.identityaccess.domain.auth.AccessToken;
import com.beautysight.liurushi.identityaccess.domain.auth.AccessTokenService;
import com.beautysight.liurushi.identityaccess.domain.auth.AccessTokenVM;
import com.beautysight.liurushi.identityaccess.ex.AuthException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.Callable;

/**
 * @author chenlong
 * @since 1.0
 */
@Service
public class OAuthApp {

    private static final Logger logger = LoggerFactory.getLogger(OAuthApp.class);

    @Autowired
    private AccessTokenService accessTokenService;

    private final AccessTokenCache accessTokenCache = new AccessTokenCache();

    public AccessToken authenticate(final AuthCommand authCommand) {
        AccessToken theToken = accessTokenCache.getIfAbsentLoad(
                authCommand.accessToken, authCommand.type, new Callable<AccessToken>() {
                    @Override
                    public AccessToken call() throws Exception {
                        logger.debug("Access token absent in cache, so load");
                        return accessTokenService.loadAccessTokenBy(
                                authCommand.accessToken, authCommand.type);
                    }
                });

        if (theToken.isMarkedAsInvalid()) {
            throw new AuthException(AuthErrorId.invalid_access_token, "Invalid %s token: %s",
                    theToken.type(),
                    theToken.accessToken());
        }

        if (theToken.isExpired() && !theToken.isMarkedAsInvalid()) {
            accessTokenService.invalidate(theToken.accessToken(), theToken.type());
            evictAccessToken(theToken.accessToken(), theToken.type());
            throw new AuthException(AuthErrorId.invalid_access_token, "Invalid %s token: %s",
                    theToken.type(),
                    theToken.accessToken());
        }

        logger.info("Authenticating access token completed");
        return theToken;
    }

    public AccessTokenVM refreshAccessToken(RefreshAccessTokenCommand command) {
        AccessToken theToken = accessTokenService.loadAccessTokenBy(command.accessToken, AccessToken.Type.Bearer);
        if (!theToken.refreshToken().equals(command.refreshToken)) {
            throw new IllegalParamException("refreshToken illegal");
        }

        if (theToken.checkIfInvalid()) {
            AccessToken newToken = accessTokenService.refreshAccessToken(theToken);
            logger.info("Access token is invalid, so refresh");
            return AccessTokenVM.of(newToken);
        }

        logger.info("Access token is still valid, so not refresh and return current token");
        return AccessTokenVM.of(theToken);
    }

    public void evictAccessToken(String accessToken, AccessToken.Type type) {
        accessTokenCache.evictBy(accessToken, type);
        logger.debug("evict access token from cache");
    }

    public AccessToken getLastTokenOrCurrentToken(String accessToken, AccessToken.Type type) {
        return accessTokenService.getAccessTokenBy(accessToken, type);
    }

}
