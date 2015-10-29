/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.domain.work.cs;

import com.beautysight.liurushi.community.domain.work.ContentType;

/**
 * @author chenlong
 * @since 1.0
 */
public class Video extends Rich {

    public Video() {
        this.type = ContentType.video;
    }

    public enum Format {
        mp4, mov
    }

}
