/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.domain.service;

import com.beautysight.liurushi.common.ex.AuthException;
import com.beautysight.liurushi.common.ex.EntityNotFoundException;
import com.beautysight.liurushi.identityaccess.common.AuthErrorId;
import com.beautysight.liurushi.identityaccess.domain.model.AccessToken;
import com.beautysight.liurushi.identityaccess.domain.model.Device;
import com.beautysight.liurushi.identityaccess.domain.model.User;
import com.beautysight.liurushi.identityaccess.domain.model.UserClient;
import com.beautysight.liurushi.identityaccess.domain.repo.AccessTokenRepo;
import com.google.common.base.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author chenlong
 * @since 1.0
 */
@Service
public class AccessTokenService {

    @Autowired
    private AccessTokenRepo accessTokenRepo;

    public AccessToken getIfAbsentIssueBasicTokenFor(Device device) {
        Optional<AccessToken> theToken = accessTokenRepo.basicTokenIssuedFor(device);
        if (theToken.isPresent()) {
            return theToken.get();
        }

        AccessToken basicToken = AccessToken.issueBasicTokenFor(device);
        accessTokenRepo.save(basicToken);
        return basicToken;
    }

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

    public AccessToken exchangeForBasicToken(AccessToken bearerToken) {
        Optional<AccessToken> basicToken = accessTokenRepo.basicTokenIssuedFor(bearerToken.device());
        if (basicToken.isPresent()) {
            return basicToken.get();
        }
        throw new EntityNotFoundException("Expect basic token present, but actual absent");
    }

    public AccessToken checkValidityAndLoad(String accessToken, AccessToken.Type type) {
        Optional<AccessToken> theToken = accessTokenRepo.accessTokenOf(accessToken, type);
        if (!theToken.isPresent()) {
            throw new AuthException(AuthErrorId.invalid_access_token, "Invalid %s token: %s", type, accessToken);
        }

        return theToken.get();
    }

    public UserClient getUserClientBy(AccessToken.Type type, String accessToken) {
        Optional<AccessToken> theToken = accessTokenRepo.accessTokenOf(accessToken, type);
        if (!theToken.isPresent()) {
            throw new EntityNotFoundException("Not found %s token: %s", type, accessToken);
        }

        return UserClient.newInstanceBy(theToken.get());
    }

}
