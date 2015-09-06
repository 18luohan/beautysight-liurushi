/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.domain.auth;

import com.beautysight.liurushi.common.ex.EntityNotFoundException;
import com.beautysight.liurushi.identityaccess.common.AuthErrorId;
import com.beautysight.liurushi.identityaccess.domain.user.Device;
import com.beautysight.liurushi.identityaccess.domain.user.User;
import com.beautysight.liurushi.identityaccess.ex.AuthException;
import com.google.common.base.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author chenlong
 * @since 1.0
 */
@Service
public class AccessTokenService {

    private static final Logger logger = LoggerFactory.getLogger(AccessTokenService.class);

    @Autowired
    private AccessTokenRepo accessTokenRepo;

    public AccessToken issueOrRefreshAccessTokenFor(User user, Device device) {
        Optional<AccessToken> accessToken = accessTokenRepo.bearerTokenIssuedFor(user.id(), device.id());
        if (accessToken.isPresent()) {
            return refreshAccessToken(accessToken.get());
        } else {
            return accessTokenRepo.save(AccessToken.issueBearerTokenFor(user, device));
        }
    }

    public AccessToken refreshAccessToken(AccessToken currentToken) {
        currentToken.refresh();
        accessTokenRepo.merge(currentToken);
        return currentToken;
    }

    public AccessToken loadAccessTokenBy(String accessToken, AccessToken.Type type) {
        // 检查是否是曾经使用过但已无效的token
        Optional<AccessToken> lastToken = accessTokenRepo.lastAccessTokenOf(accessToken, type);
        if (lastToken.isPresent()) {
            throw new AuthException(AuthErrorId.invalid_access_token,
                    "Invalid %s token: %s, because it's refreshed", type, accessToken);
        }

        // 检查是否是不存在的token，即伪造的token
        Optional<AccessToken> theToken = accessTokenRepo.accessTokenOf(accessToken, type);
        if (!theToken.isPresent()) {
            throw new AuthException(AuthErrorId.illegal_access_token, "Illegal %s token: %s", type, accessToken);
        }
        return theToken.get();
    }

    public AccessToken getAccessTokenBy(String accessToken, AccessToken.Type type) {
        Optional<AccessToken> lastToken = accessTokenRepo.lastAccessTokenOf(accessToken, type);
        if (lastToken.isPresent()) {
            return lastToken.get();
        }

        // 检查是否是不存在的token，即伪造的token
        Optional<AccessToken> theToken = accessTokenRepo.accessTokenOf(accessToken, type);
        if (!theToken.isPresent()) {
            throw new AuthException(AuthErrorId.illegal_access_token, "Illegal %s token: %s", type, accessToken);
        }
        return theToken.get();
    }

    public void invalidate(String accessToken, AccessToken.Type type) {
        Optional<AccessToken> theToken = accessTokenRepo.accessTokenOf(accessToken, type);
        if (!theToken.isPresent()) {
            throw new EntityNotFoundException(
                    "Expected access token present, but actual absent: %s", accessToken);
        }

        theToken.get().invalidate();
        accessTokenRepo.merge(theToken.get());
        logger.debug("Invalidate access token");
    }

}