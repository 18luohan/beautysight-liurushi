/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.domain.model.work.picstory;

import com.beautysight.liurushi.community.domain.model.work.cs.Picture;
import org.mongodb.morphia.annotations.Reference;

/**
 * @author chenlong
 * @since 1.0
 */
public class Cover {

    @Reference(value = "sectionId", idOnly = true)
    private Picture picture;
    private PictureArea picArea;

    public void setPicture(Picture picture) {
        this.picture = picture;
    }

    public String pictureKey() {
        return this.picture.key();
    }

}