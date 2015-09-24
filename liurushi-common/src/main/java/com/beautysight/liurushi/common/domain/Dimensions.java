/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.common.domain;

import com.beautysight.liurushi.common.utils.PreconditionUtils;

/**
 * Represents the size of something using width and height. Any other class that represents
 * the width and height of something should extend this class.
 *
 * @author chenlong
 * @since 1.0
 */
public class Dimensions extends ValueObject {

    private Integer width;
    private Integer height;

    public Integer width() {
        return this.width;
    }

    public Integer height() {
        return this.height;
    }

    public void validate() {
        PreconditionUtils.checkGreaterThanZero(format("%s.width"), width);
        PreconditionUtils.checkGreaterThanZero(format("%s.height"), height);
    }

}