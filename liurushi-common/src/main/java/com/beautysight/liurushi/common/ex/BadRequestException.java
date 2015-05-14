/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.common.ex;

/**
 * Here is Javadoc.
 * <p/>
 * Created by chenlong on 2015-05-14.
 *
 * @author chenlong
 * @since 1.0
 */
public class BadRequestException extends ApplicationException {

    public BadRequestException(Error.Id errorId, String message) {
        super(errorId, message);
    }

    public BadRequestException(Error.Id errorId, String message, Throwable cause) {
        super(errorId, message, cause);
    }

}
