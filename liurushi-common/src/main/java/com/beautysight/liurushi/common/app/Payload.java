/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.common.app;

import com.beautysight.liurushi.common.domain.JsonAnyFieldVisible;

import java.io.Serializable;

/**
 * Domain Payload Object
 * @author chenlong
 * @since 1.0
 */
public abstract class Payload implements JsonAnyFieldVisible, Serializable {

    protected String format(String msg) {
        return String.format(msg, shortName());
    }

    protected String shortName() {
        return this.getClass().getSimpleName();
    }

}