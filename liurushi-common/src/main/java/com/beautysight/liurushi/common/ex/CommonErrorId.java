/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.common.ex;

/**
 * Here is Javadoc.
 * <p/>
 * Created by chenlong on 2015-05-08.
 *
 * @author chenlong
 * @since 1.0
 */
public enum CommonErrorId implements Error.Id {

    bad_request(10001),
    invalid_params(10002),
    business_constraint_violated(10003),
    server_data_stale(10004),
    internal_server_error(10005),
    service_unavailable(10006);

    private Integer code;

    CommonErrorId(Integer code) {
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
