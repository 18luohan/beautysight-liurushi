/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.app.cache;

import com.beautysight.liurushi.identityaccess.app.command.AccessTokenDTO;
import com.beautysight.liurushi.identityaccess.domain.model.AccessToken;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author chenlong
 * @since 1.0
 */
public class AccessTokenAsKeyTest {

    @Test
    public void testEquals() throws Exception {
        AccessTokenDTO dto = new AccessTokenDTO(AccessToken.Type.Bearer, "bearer_token");
        AccessTokenCache.CacheKey key1 = new AccessTokenCache.CacheKey(dto);
        AccessTokenCache.CacheKey key2 = new AccessTokenCache.CacheKey(dto);
        assertTrue(key1.equals(key2));
    }

    @Test
    public void testNotEquals() throws Exception {
        AccessTokenDTO dto1 = new AccessTokenDTO(AccessToken.Type.Bearer, "bearer_token1");
        AccessTokenCache.CacheKey key1 = new AccessTokenCache.CacheKey(dto1);

        AccessTokenDTO dto2 = new AccessTokenDTO(AccessToken.Type.Bearer, "bearer_token2");
        AccessTokenCache.CacheKey key2 = new AccessTokenCache.CacheKey(dto2);
        assertFalse(key1.equals(key2));
    }

    @Test
    public void testHashCode() throws Exception {
        AccessTokenDTO dto = new AccessTokenDTO(AccessToken.Type.Bearer, "bearer_token");
        AccessTokenCache.CacheKey key1 = new AccessTokenCache.CacheKey(dto);
        AccessTokenCache.CacheKey key2 = new AccessTokenCache.CacheKey(dto);
        assertEquals(key1.hashCode(), key2.hashCode());
    }
}