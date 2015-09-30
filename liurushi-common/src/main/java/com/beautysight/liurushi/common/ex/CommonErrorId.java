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

    internal_server_error(10001),
    service_unavailable(10002),

    bad_request(10003),
    no_permission_for_this_api(10007),

    invalid_params(10008),
    business_constraint_violated(10009),
    server_data_stale(10010);

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
