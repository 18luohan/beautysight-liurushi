/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.domain.work.layout;

import com.beautysight.liurushi.common.domain.ValueObject;
import com.beautysight.liurushi.common.utils.PreconditionUtils;

/**
 * @author chenlong
 * @since 1.0
 */
public class Layout extends ValueObject {

    private Integer rows;
    private Integer cols;

    public void validate() {
        PreconditionUtils.checkGreaterThanZero("Layout.rows", rows);
        PreconditionUtils.checkGreaterThanZero("Layout.cols", cols);
    }

    public BlockLocator generateBlockLocator() {
        return new BlockLocator(this.cols);
    }

}