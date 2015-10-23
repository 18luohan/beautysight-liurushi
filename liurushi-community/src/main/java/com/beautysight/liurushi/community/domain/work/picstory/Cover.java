/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.domain.work.picstory;

import com.beautysight.liurushi.community.domain.work.cs.Rich;
import org.mongodb.morphia.annotations.Reference;

/**
 * @author chenlong
 * @since 1.0
 */
public class Cover {

    @Reference(value = "sectionId", idOnly = true)
    private Rich content;
    private PictureArea picArea;

    public void setContent(Rich content) {
        this.content = content;
    }

    public Rich getContent() {
        return this.content;
    }

    public String contentFileKey() {
        return this.content.fileKey();
    }

}