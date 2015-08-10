/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.domain.model.work.picstory;

import com.beautysight.liurushi.common.domain.ValueObject;
import com.beautysight.liurushi.common.utils.PreconditionUtils;
import com.beautysight.liurushi.community.domain.model.work.Control;

/**
 * 镜头。镜头就是指所拍摄到的画面。镜头里可以是图画，也可以是文字。
 *
 * @author chenlong
 * @since 1.0
 */
public class Shot extends Control {

    private Size size;
    private PictureArea picAreaInShot;

    // 对发表成功的作品来说，该属性可选。
    private PictureArea picVisibleArea;

    public static class Size extends ValueObject {
        private Integer rowSpan;
        private Integer colSpan;

        public void validate() {
            PreconditionUtils.checkGreaterThanZero("ControlSize(e.g. shot).rowSpan", rowSpan);
            PreconditionUtils.checkGreaterThanZero("ControlSize(e.g. shot).colSpan", colSpan);
        }
    }

}