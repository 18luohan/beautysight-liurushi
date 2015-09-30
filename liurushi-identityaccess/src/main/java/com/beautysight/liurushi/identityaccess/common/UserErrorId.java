/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.common;

import com.beautysight.liurushi.common.ex.Error;

/**
 * Here is Javadoc.
 * <p/>
 * Created by chenlong on 2015-05-08.
 *
 * @author chenlong
 * @since 1.0
 */
public enum UserErrorId implements Error.Id {

    user_already_exist(20101),
    password_confirmpwd_not_equal(20102),
    user_not_exist_or_pwd_incorrect(20103),
    user_not_exist(20104);

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
        return null;
    }

}
