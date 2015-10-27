/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.common;

import com.beautysight.liurushi.common.ex.Error;

/**
 * @author chenlong
 * @since 1.0
 */
public enum UserErrorId implements Error.Id {

    user_already_exist(21001),
    password_confirmpwd_not_equal(21002),
    user_not_exist_or_pwd_incorrect(21003),
    user_not_exist(21004);

    private Integer code;

    UserErrorId(Integer code) {
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
