/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.fundamental.infrastructure.persistence.mongo;

import org.springframework.dao.DataAccessException;

/**
 * Here is Javadoc.
 * <p/>
 * Created by chenlong on 2015-05-12.
 *
 * @author chenlong
 * @since 1.0
 */
public class IncorrectOperationResultException extends DataAccessException {

    public IncorrectOperationResultException(String message) {
        super(message);
    }

    public IncorrectOperationResultException(String message, Throwable cause) {
        super(message, cause);
    }

}
