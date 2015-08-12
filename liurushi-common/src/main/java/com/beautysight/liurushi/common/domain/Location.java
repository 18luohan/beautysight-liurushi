/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.common.domain;

import com.beautysight.liurushi.common.utils.PreconditionUtils;

/**
 * @author chenlong
 * @since 1.0
 */
public class Location extends ValueObject {

    /**
     * 与一个宽度相比的百分点数，而非像素数。
     */
    private Integer xPct;
    /**
     * 与一个高度相比的百分点数，而非像素数。
     */
    private Integer yPct;

    public void validate() {
        PreconditionUtils.checkGreaterThanOrEq(-1, "location.xPct", xPct);
        PreconditionUtils.checkGreaterThanOrEq(-1, "location.yPct", yPct);
    }

}