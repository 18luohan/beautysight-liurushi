/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.domain.service;

import com.beautysight.liurushi.identityaccess.ex.AuthException;
import com.beautysight.liurushi.common.ex.EntityNotFoundException;
import com.beautysight.liurushi.identityaccess.common.AuthErrorId;
import com.beautysight.liurushi.identityaccess.domain.model.AccessToken;
import com.beautysight.liurushi.identityaccess.domain.model.Device;
import com.beautysight.liurushi.identityaccess.domain.model.User;
import com.beautysight.liurushi.identityaccess.domain.repo.AccessTokenRepo;
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

    public AccessToken issueOrRefreshBearerTokenFor(User user, Device device) {
        Optional<AccessToken> accessToken = accessTokenRepo.bearerTokenIssuedFor(user.id(), device.id());
        if (accessToken.isPresent()) {
            return refreshBearerToken(accessToken.get());
        } else {
            return accessTokenRepo.save(AccessToken.issueBearerTokenFor(user, device));
        }
    }

    public AccessToken refreshBearerToken(AccessToken currentToken) {
        currentToken.refresh();
        accessTokenRepo.merge(currentToken);
        return currentToken;
    }

    public AccessToken checkAndLoad(String accessToken, AccessToken.Type type) {
        // 检查是否是曾经使用过但已无效的token
        Optional<AccessToken> lastToken = accessTokenRepo.lastAccessTokenOf(accessToken, type);
        if (lastToken.isPresent()) {
            throw new AuthException(AuthErrorId.invalid_access_token, "Invalid %s token: %s", type, accessToken);
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

    public User getUserBy(String type, String accessToken) {
        Optional<AccessToken> theToken = accessTokenRepo.accessTokenOf(accessToken, AccessToken.Type.valueOf(type));
        if (!theToken.isPresent()) {
            throw new EntityNotFoundException("Not found %s token: %s", type, accessToken);
        }

        return theToken.get().user();
    }

}
