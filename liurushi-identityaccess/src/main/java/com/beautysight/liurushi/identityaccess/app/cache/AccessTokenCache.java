/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.app.cache;

import com.beautysight.liurushi.common.ex.ApplicationException;
import com.beautysight.liurushi.common.ex.CacheException;
import com.beautysight.liurushi.common.utils.Beans;
import com.beautysight.liurushi.common.utils.GuavaCaches;
import com.beautysight.liurushi.identityaccess.domain.model.AccessToken;
import com.google.common.cache.Cache;

import java.util.Date;
import java.util.concurrent.Callable;

/**
 * @author chenlong
 * @since 1.0
 */
public class AccessTokenCache {

    private final Cache<AccessTokenAsCacheKey, CacheValue> userClientCache = GuavaCaches.createCache();

    public AccessToken getIfAbsentLoad(final String accessToken, final AccessToken.Type type, final Callable<AccessToken> loader) {
        try {
            CacheValue value = userClientCache.get(
                    new AccessTokenAsCacheKey(accessToken, type),
                    new Callable<CacheValue>() {
                        @Override
                        public CacheValue call() throws Exception {
                            AccessToken accessToken = loader.call();
                            return CacheValue.from(accessToken);
                        }
                    });
            return value.toDomainEntity();
        } catch (Exception ex) {
            throw handleException(ex);
        }
    }

    public void evictBy(String accessToken, AccessToken.Type type) {
        userClientCache.invalidate(new AccessTokenAsCacheKey(accessToken, type));
    }

    private ApplicationException handleException(final Exception ex) {
        if (ex.getCause() instanceof ApplicationException) {
            return (ApplicationException) ex.getCause();
        }
        return new CacheException("Unexpected error", ex);
    }

    public static class CacheValue {
        private AccessToken.Type type;
        private String accessToken;
        private Date createdAt;
        private Date refreshedAt;
        private int expiresIn;

        public static CacheValue from(AccessToken domainEntity) {
            CacheValue target = new CacheValue();
            Beans.copyProperties(domainEntity, target);
            return target;
        }

        public AccessToken toDomainEntity() {
            AccessToken target = new AccessToken();
            Beans.copyProperties(this, target);
            return target;
        }
    }

}
