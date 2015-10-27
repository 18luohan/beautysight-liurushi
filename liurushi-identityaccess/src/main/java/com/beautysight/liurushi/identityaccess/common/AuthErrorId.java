/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.common;

import com.beautysight.liurushi.common.ex.Error;

/**
 * @author chenlong
 * @since 1.0
 */
public enum AuthErrorId implements Error.Id {

    illegal_app_id(10101),
    illegal_access_token(10102),
    invalid_access_token(10103),
    no_permission_for_this_api(10104);

    private Integer code;

    AuthErrorId(Integer code) {
        this.code = code;
    }

    @Override
    public String get() {
        return this.toString();
    }

    @Override
    public Integer code() {
        return this.code;
    }

}