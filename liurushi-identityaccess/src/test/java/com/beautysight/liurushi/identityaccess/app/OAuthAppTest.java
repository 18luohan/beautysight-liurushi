/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.app;

import com.beautysight.liurushi.common.ex.AuthException;
import com.beautysight.liurushi.common.utils.GuavaCaches;
import com.beautysight.liurushi.identityaccess.app.cache.AccessTokenCache;
import com.beautysight.liurushi.identityaccess.app.command.AuthCommand;
import com.beautysight.liurushi.identityaccess.app.command.DeviceDTO;
import com.beautysight.liurushi.identityaccess.app.presentation.AccessTokenPresentation;
import com.beautysight.liurushi.identityaccess.common.AuthErrorId;
import com.beautysight.liurushi.identityaccess.domain.model.AccessToken;
import com.beautysight.liurushi.identityaccess.domain.model.User;
import com.beautysight.liurushi.identityaccess.domain.model.UserClient;
import com.beautysight.liurushi.identityaccess.domain.repo.DeviceRepo;
import com.beautysight.liurushi.identityaccess.domain.service.AccessTokenService;
import com.beautysight.liurushi.test.utils.JsonsForTest;
import com.beautysight.liurushi.test.utils.Reflections;
import com.google.common.cache.Cache;
import com.google.common.util.concurrent.UncheckedExecutionException;
import mockit.*;
import mockit.integration.junit4.JMockit;
import org.bson.types.ObjectId;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.*;

/**
 * @author chenlong
 * @since 1.0
 */
@RunWith(JMockit.class)
public class OAuthAppTest {

    @Tested
    private OAuthApp oAuthApp;

    @Injectable
    private AccessTokenService accessTokenService;
    @Injectable
    private DeviceRepo deviceRepo;

    // dynamically partial mocking
    private Cache<AccessTokenCache.CacheKey, UserClient> userClientCache = GuavaCaches.createCache();

    private AccessTokenCache.CacheKey key = new AccessTokenCache.CacheKey(new AuthCommand(AccessToken.Type.Bearer, "any_token"));
    private UserClient userClient = newUserClient();

    @Test
    public void testGetIfAbsentIssueBasicTokenFor() throws Exception {
        DeviceDTO device = JsonsForTest.readJsonFileInSameDirWithTestClassAsModel(
                OAuthAppTest.class, "device.json", DeviceDTO.class);
        AccessTokenPresentation accessToken = oAuthApp.getIfAbsentIssueBasicTokenFor(device);
        assertNotNull(accessToken);
    }

    @Test
    public void notCachedSoComputeCacheReturn() {
//        final UserClient expectedUserClient = newUserClient();
//
//        new Expectations() {{
//            accessTokenService.authenticate(anyString, AccessToken.Type.Basic);
//            result = expectedUserClient;
//        }};
//
//
//        AccessTokenDTO dpo = new AccessTokenDTO(AccessToken.Type.Basic, "any_token");
//        final UserClient realUserClient = oAuthApp.authenticate(dpo);
//
//        new Verifications() {{
//            assertTrue(realUserClient.deviceId() == expectedUserClient.deviceId());
//            accessTokenService.authenticate(anyString, AccessToken.Type.Basic); times = 1;
//        }};
    }

    @Test
    public void cachedSoReturn() throws ExecutionException {
//        final UserClient expectedUserClient = newUserClient();
//
//        new Expectations() {{
//            userClientCache.get((AccessTokenCache.CacheKey) any, (Callable<UserClient>) any);
//            result = expectedUserClient;
//        }};
//
//        AccessTokenDTO dpo = new AccessTokenDTO(AccessToken.Type.Basic, "any_token");
//        final UserClient realUserClient = oAuthApp.authenticate(dpo);
//
//        new Verifications() {{
//            assertTrue(realUserClient.deviceId() == expectedUserClient.deviceId());
//            accessTokenService.authenticate(anyString, (AccessToken.Type) any); times = 0;
//        }};
    }

    @Before
    public void prepareCachedUserClient() {
        userClientCache.put(key, userClient);
    }

    @After
    public void clearUserClientCache() {
        userClientCache.invalidateAll();
    }

    @Test(expected = AuthException.class)
    public void tokenExpiredSoEvictFromCacheAndThrowAuthException() throws ExecutionException {
        new Expectations(userClientCache) {{
            userClientCache.get((AccessTokenCache.CacheKey) any, (Callable<UserClient>) any);
            AuthException cause = new AuthException(AuthErrorId.expired_access_token, "Expired bearer token: any_token");
            result = new UncheckedExecutionException(cause);
            times = 1;

            userClientCache.invalidate(any); times = 1;
        }};

        AuthCommand dto = new AuthCommand(AccessToken.Type.Bearer, "any_token");
        oAuthApp.authenticate(dto);

        UserClient cachedUserClient = userClientCache.getIfPresent(key);
        assertNull(cachedUserClient);
    }

    private UserClient newUserClient() {
        UserClient userClient = Reflections.newInstanceUsingDefaultConstructor(UserClient.class);
        Reflections.setField(userClient, "deviceId", new ObjectId("558a28c2b2f4bd263115b3f8"));
        Reflections.setField(userClient, "userType", User.Type.visitor);
        return userClient;
    }

}