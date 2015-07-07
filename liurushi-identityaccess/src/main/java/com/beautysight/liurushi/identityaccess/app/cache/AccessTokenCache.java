/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.app.cache;

import com.beautysight.liurushi.common.ex.ApplicationException;
import com.beautysight.liurushi.common.ex.CacheException;
import com.beautysight.liurushi.common.utils.Beans;
import com.beautysight.liurushi.common.utils.GuavaCaches;
import com.beautysight.liurushi.identityaccess.app.command.AccessTokenDTO;
import com.beautysight.liurushi.identityaccess.domain.model.AccessToken;
import com.google.common.cache.Cache;

import java.util.Date;
import java.util.concurrent.Callable;

/**
 * @author chenlong
 * @since 1.0
 */
public class AccessTokenCache {

    private final Cache<CacheKey, CacheValue> userClientCache = GuavaCaches.createCache();

    public AccessToken getIfAbsentLoad(final AccessTokenDTO accessTokenDTO, final Callable<AccessToken> loader) {
        try {
            CacheValue value = userClientCache.get(
                    new CacheKey(accessTokenDTO),
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

    public void evictBy(final AccessTokenDTO accessTokenDTO) {
        userClientCache.invalidate(new CacheKey(accessTokenDTO));
    }

    private ApplicationException handleException(final Exception ex) {
        if (ex.getCause() instanceof ApplicationException) {
            return (ApplicationException) ex.getCause();
        }
        return new CacheException("Unexpected error", ex);
    }

    /**
     * 用作缓存的key，包含 access toke 信息。
     * @author chenlong
     * @since 1.0
     */
    public static final class CacheKey {

        private AccessToken.Type type;
        private String token;

        public CacheKey(AccessTokenDTO dto) {
            this.type = dto.type;
            this.token = dto.accessToken;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            CacheKey that = (CacheKey) o;

            if (type != that.type) return false;
            return token.equals(that.token);
        }

        @Override
        public int hashCode() {
            int result = type.hashCode();
            result = 31 * result + token.hashCode();
            return result;
        }

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
