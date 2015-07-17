/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.common.shared;

/**
 * Domain model super type
 * @author chenlong
 * @since 1.0
 */
public abstract class DomainModel {

    protected String format(String msg) {
        return String.format(msg, thisClassSimpleName());
    }

    protected String thisClassSimpleName() {
        return this.getClass().getSimpleName();
    }

}
