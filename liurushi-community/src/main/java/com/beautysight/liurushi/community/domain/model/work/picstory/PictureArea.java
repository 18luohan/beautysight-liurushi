/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.domain.model.work.picstory;

import com.beautysight.liurushi.common.domain.Location;
import com.beautysight.liurushi.common.domain.ValueObject;
import com.beautysight.liurushi.common.utils.PreconditionUtils;

import java.util.UUID;

/**
 * @author chenlong
 * @since 1.0
 */
public class PictureArea extends ValueObject {

    /**
     * 百分数表示的缩放比例
     */
    private Integer scalingPct;
    private Location startLoc;
    private Location endLoc;

    public void validate() {
        PreconditionUtils.checkRequired("PictureArea.startLoc", startLoc);
        PreconditionUtils.checkRequired("PictureArea.endLoc", endLoc);
        startLoc.validate();
        endLoc.validate();
    }

    public static void main(String[] args) {
        System.out.println(UUID.randomUUID());
    }

}