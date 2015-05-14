/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.common.ex;

/**
 * Exception thrown when only one entity was expected, but actually found multiple ones.
 *
 * @author chenlong
 * @since 1.0
 */
public class NoUniqueEntityException extends ApplicationException {

    public NoUniqueEntityException(String message) {
        super(CommonErrorId.server_data_stale, message);
    }

}
