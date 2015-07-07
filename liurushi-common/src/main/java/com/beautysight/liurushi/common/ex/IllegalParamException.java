/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.common.ex;

/**
 * @author chenlong
 * @since 1.0
 */
public class IllegalParamException extends ApplicationException {

    public IllegalParamException(String message) {
        super(CommonErrorId.invalid_params, message);
    }

    public IllegalParamException(String msgFormat, Object... msgArgs) {
        super(CommonErrorId.invalid_params, msgFormat, msgArgs);
    }

    public IllegalParamException(Error.Id errorId, String message) {
        super(errorId, message);
    }

}
