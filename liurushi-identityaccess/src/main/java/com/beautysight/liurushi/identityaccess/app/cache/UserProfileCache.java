/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.app.cache;

import com.beautysight.liurushi.common.ex.ApplicationException;
import com.beautysight.liurushi.common.ex.CacheException;
import com.beautysight.liurushi.common.utils.GuavaCaches;
import com.beautysight.liurushi.identityaccess.domain.model.AccessToken;
import com.beautysight.liurushi.identityaccess.domain.model.UserLite;
import com.google.common.cache.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;

/**
 * @author chenlong
 * @since 1.0
 */
public class UserProfileCache {

    private static final Logger logger = LoggerFactory.getLogger(UserProfileCache.class);

    private final Cache<AccessTokenAsCacheKey, UserLite> userProfileCache = GuavaCaches.createCache();

    public UserLite getIfAbsentLoad(final String accessToken, final AccessToken.Type type, final Callable<UserLite> loader) {
        try {
            return userProfileCache.get(
                    new AccessTokenAsCacheKey(accessToken, type),
                    new Callable<UserLite>() {
                        @Override
                        public UserLite call() throws Exception {
                            return loader.call();
                        }
                    });
        } catch (Exception ex) {
            throw handleException(ex);
        }
    }

    public void put(String accessToken, AccessToken.Type type, UserLite userLite) {
        userProfileCache.put(new AccessTokenAsCacheKey(accessToken, type), userLite);
        logger.debug("Put user profile into cache");
    }

    public void evictBy(String accessToken, AccessToken.Type type) {
        userProfileCache.invalidate(new AccessTokenAsCacheKey(accessToken, type));
        logger.debug("Evict user profile from cache");
    }

    private ApplicationException handleException(final Exception ex) {
        if (ex.getCause() instanceof ApplicationException) {
            return (ApplicationException) ex.getCause();
        }
        return new CacheException("Unexpected error", ex);
    }

}