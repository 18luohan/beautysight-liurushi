/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.common;

import com.beautysight.liurushi.common.ex.Error;

/**
 * Here is Javadoc.
 * <p/>
 * Created by chenlong on 2015-05-30.
 *
 * @author chenlong
 * @since 1.0
 */
public enum AuthErrorId implements Error.Id {

    illegal_app_id(10004),
    illegal_access_token(10005),
    invalid_access_token(10006);

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