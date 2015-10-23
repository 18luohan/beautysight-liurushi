/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.domain.work.cs;

/**
 * @author chenlong
 * @since 1.0
 */
public class Video extends Rich {

    public Video() {
        this.type = Type.video;
    }

    public enum Format {
        mp4, mov
    }

}
