/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.domain.work.cs;

/**
 * 图片
 *
 * @author chenlong
 * @since 1.0
 */
public class Picture extends Rich {

    public Picture() {
        this.type = Type.image;
    }

    public enum Format {
        jpg, jpeg, png, bmp, webp
    }

}