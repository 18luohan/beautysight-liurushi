/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.fundamental.domain.appconfig;

import com.beautysight.liurushi.common.domain.ValueObject;
import com.google.common.base.Preconditions;

/**
 * @author chenlong
 * @since 1.0
 */
public class IntegerVal extends ValueObject {

    private Integer val;

    public Integer val() {
        Preconditions.checkState((val != null), "val must not be null");
        return this.val;
    }

}
