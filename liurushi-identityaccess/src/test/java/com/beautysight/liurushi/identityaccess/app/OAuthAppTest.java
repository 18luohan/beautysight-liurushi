/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.app;

import com.beautysight.liurushi.identityaccess.ex.AuthException;
import com.beautysight.liurushi.common.utils.Jsons;
import com.beautysight.liurushi.identityaccess.app.cache.AccessTokenCache;
import com.beautysight.liurushi.identityaccess.app.command.AuthCommand;
import com.beautysight.liurushi.identityaccess.domain.model.AccessToken;
import com.beautysight.liurushi.identityaccess.domain.repo.AccessTokenRepo;
import com.beautysight.liurushi.identityaccess.domain.repo.DeviceRepo;
import com.beautysight.liurushi.identityaccess.domain.service.AccessTokenService;
import com.beautysight.liurushi.test.utils.Files;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.ExecutionException;

/**
 * @author chenlong
 * @since 1.0
 */
@RunWith(JMockit.class)
public class OAuthAppTest {

    @Tested
    private OAuthApp oAuthApp;

    @Injectable
    private DeviceRepo deviceRepo;
    @Injectable
    private AccessTokenRepo accessTokenRepo;
    @Injectable
    private AccessTokenService accessTokenService;

    // dynamically partial mocking
    @Injectable
    private AccessTokenCache accessTokenCache = new AccessTokenCache();

    private AccessToken accessToken = newAccessToken();

    @Test
    public void notCachedSoComputeAndCacheAndReturn() {
        new Expectations() {{
            accessTokenService.loadAccessTokenBy(anyString, (AccessToken.Type) any);
            result = accessToken;
        }};

        final AuthCommand command = new AuthCommand(AccessToken.Type.Bearer, "any_access_token");
        oAuthApp.authenticate(command);
//        final AccessToken theCachedToken = accessTokenCache.getIfAbsentLoad(
//                command.type, command.accessToken, new Callable<AccessToken>() {
//                    @Override
//                    public AccessToken call() throws Exception {
//                        throw new RuntimeException("this method should not be invoked");
//                    }
//                });

        new Verifications() {{
            accessTokenService.loadAccessTokenBy(anyString, AccessToken.Type.Bearer);
            times = 1;
//            assertEquals(command.type, theCachedToken.type());
//            assertEquals(command.accessToken, theCachedToken.accessToken());
        }};
    }

    @Test
    public void cachedSoNotComputeJustReturn() {
        new Expectations() {{
            accessTokenService.loadAccessTokenBy(anyString, (AccessToken.Type) any);
            result = accessToken;
        }};

        final AuthCommand command = new AuthCommand(AccessToken.Type.Bearer, "any_access_token");
        oAuthApp.authenticate(command);
        oAuthApp.authenticate(command);

        new Verifications() {{
            accessTokenService.loadAccessTokenBy(anyString, (AccessToken.Type) any);
            times = 1;
        }};
    }

//    @Before
//    public void prepareCachedUserClient() {
//        userProfileCache.put(key, userClient);
//    }
//
//    @After
//    public void clearUserClientCache() {
//        userProfileCache.invalidateAll();
//    }

    @Test(expected = AuthException.class)
    public void tokenExpiredSoEvictFromCacheAndThrowAuthException() throws ExecutionException {
//        new Expectations(userProfileCache) {{
//            userProfileCache.get((AccessTokenAsCacheKey) any, (Callable<UserClient>) any);
//            AuthException cause = new AuthException(AuthErrorId.invalid_access_token, "Expired bearer token: any_token");
//            result = new UncheckedExecutionException(cause);
//            times = 1;
//
//            userProfileCache.invalidate(any);
//            times = 1;
//        }};
//
//        AuthCommand dto = new AuthCommand(AccessToken.Type.Bearer, "any_token");
//        oAuthApp.authenticate(dto);
//
//        UserClient cachedUserClient = userProfileCache.getIfPresent(key);
//        assertNull(cachedUserClient);
    }

    private AccessToken newAccessToken() {
        return Jsons.toObject(Files.fileInSameDirWith(OAuthAppTest.class, "AccessToken.json"), AccessToken.class);
    }

}