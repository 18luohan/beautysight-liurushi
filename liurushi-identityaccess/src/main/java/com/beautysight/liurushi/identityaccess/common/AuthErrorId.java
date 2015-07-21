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

    invalid_app_id,
    invalid_access_token,
    expired_access_token;

    @Override
    public String get() {
        return this.toString();
    }

}
