/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.domain.model.content;

import com.beautysight.liurushi.common.domain.Dimensions;
import com.beautysight.liurushi.fundamental.domain.storage.ResourceInStorage;
import org.mongodb.morphia.annotations.Reference;

/**
 * 图片
 *
 * @author chenlong
 * @since 1.0
 */
public class Picture extends ContentSection {

    private String name;
    private Format format;
    private Dimensions dimensions;
    private Integer size;
    private String signature;
    private ResourceInStorage resource;

    /**
     * 原图。有些图片是基于原图处理得到的。
     */
    @Reference("originalId")
    private Picture original;

    public Picture() {
        this.initialize();
    }

    private void initialize() {
        this.type = Type.image;
    }

    public String key() {
        return resource.getKey();
    }

    public enum Format {
        jpg, jpeg, png, bmp, webp
    }

}
