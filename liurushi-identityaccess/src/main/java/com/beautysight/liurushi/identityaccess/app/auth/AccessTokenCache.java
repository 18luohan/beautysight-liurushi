/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.app.auth;

import com.beautysight.liurushi.common.ex.ApplicationException;
import com.beautysight.liurushi.common.ex.CacheException;
import com.beautysight.liurushi.common.utils.GuavaCaches;
import com.beautysight.liurushi.identityaccess.domain.auth.AccessToken;
import com.google.common.cache.Cache;

import java.util.concurrent.Callable;

/**
 * @author chenlong
 * @since 1.0
 */
public class AccessTokenCache {

    private final Cache<AccessTokenAsCacheKey, AccessToken> accessTokenCache = GuavaCaches.createCache();

    public AccessToken getIfAbsentLoad(final String accessToken, final AccessToken.Type type, final Callable<AccessToken> loader) {
        try {
            return accessTokenCache.get(
                    new AccessTokenAsCacheKey(accessToken, type),
                    new Callable<AccessToken>() {
                        @Override
                        public AccessToken call() throws Exception {
                            return loader.call();
                        }
                    });
        } catch (Exception ex) {
            throw handleException(ex);
        }
    }

    public void evictBy(String accessToken, AccessToken.Type type) {
        accessTokenCache.invalidate(new AccessTokenAsCacheKey(accessToken, type));
    }

    private ApplicationException handleException(final Exception ex) {
        if (ex.getCause() instanceof ApplicationException) {
            return (ApplicationException) ex.getCause();
        }
        return new CacheException("Unexpected error", ex);
    }

}
