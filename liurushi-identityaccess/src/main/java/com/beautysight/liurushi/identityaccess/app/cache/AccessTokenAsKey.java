/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.app.cache;

import com.beautysight.liurushi.identityaccess.app.command.AccessTokenDTO;
import com.beautysight.liurushi.identityaccess.domain.model.AccessToken;

/**
 * 用作缓存的key，包含 access toke 信息。
 * @author chenlong
 * @since 1.0
 */
public final class AccessTokenAsKey {

    private AccessToken.Type type;
    private String token;

    public AccessTokenAsKey(AccessTokenDTO dto) {
        this.type = dto.type;
        this.token = dto.accessToken;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AccessTokenAsKey that = (AccessTokenAsKey) o;

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
