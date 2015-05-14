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
public class DuplicateEntityException extends ApplicationException {

    public DuplicateEntityException(Error.Id errorId, String message) {
        super(errorId, message);
    }

}
