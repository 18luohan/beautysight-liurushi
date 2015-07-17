/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.common.ex;

/**
 * @author chenlong
 * @since 1.0
 */
public class StorageException extends ApplicationException {

    public StorageException(String message) {
        super(message);
    }

    public StorageException(String msgFormat, Object... msgArgs) {
        super(msgFormat, msgArgs);
    }

    public StorageException(String message, Throwable cause) {
        super(message, cause);
    }

    public StorageException(Throwable cause, String msgFormat, Object... msgArgs) {
        super(cause, msgFormat, msgArgs);
    }

}
