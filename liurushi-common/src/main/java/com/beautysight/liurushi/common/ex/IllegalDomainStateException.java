/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.common.ex;

/**
 * Here is Javadoc.
 * <p/>
 * Created by chenlong on 2015-05-28.
 *
 * @author chenlong
 * @since 1.0
 */
public class IllegalDomainStateException extends BusinessException {

    public IllegalDomainStateException(String message) {
        super(CommonErrorId.server_data_stale, message);
    }

    public IllegalDomainStateException(String msgFormat, Object... msgArgs) {
        super(CommonErrorId.server_data_stale, msgFormat, msgArgs);
    }

}
