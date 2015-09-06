/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.app.cache;

import com.beautysight.liurushi.identityaccess.app.auth.AccessTokenAsCacheKey;
import com.beautysight.liurushi.identityaccess.app.auth.AuthCommand;
import com.beautysight.liurushi.identityaccess.domain.auth.AccessToken;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author chenlong
 * @since 1.0
 */
public class AccessTokenAsKeyTest {

    @Test
    public void testEquals() throws Exception {
        AuthCommand dto = new AuthCommand(AccessToken.Type.Bearer, "bearer_token");
        AccessTokenAsCacheKey key1 = new AccessTokenAsCacheKey(dto.accessToken, dto.type);
        AccessTokenAsCacheKey key2 = new AccessTokenAsCacheKey(dto.accessToken, dto.type);
        assertTrue(key1.equals(key2));
    }

    @Test
    public void testNotEquals() throws Exception {
        AuthCommand dto1 = new AuthCommand(AccessToken.Type.Bearer, "bearer_token1");
        AccessTokenAsCacheKey key1 = new AccessTokenAsCacheKey(dto1.accessToken, dto1.type);

        AuthCommand dto2 = new AuthCommand(AccessToken.Type.Bearer, "bearer_token2");
        AccessTokenAsCacheKey key2 = new AccessTokenAsCacheKey(dto2.accessToken, dto2.type);
        assertFalse(key1.equals(key2));
    }

    @Test
    public void testHashCode() throws Exception {
        AuthCommand dto = new AuthCommand(AccessToken.Type.Bearer, "bearer_token");
        AccessTokenAsCacheKey key1 = new AccessTokenAsCacheKey(dto.accessToken, dto.type);
        AccessTokenAsCacheKey key2 = new AccessTokenAsCacheKey(dto.accessToken, dto.type);
        assertEquals(key1.hashCode(), key2.hashCode());
    }
}