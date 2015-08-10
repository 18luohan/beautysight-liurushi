/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.domain.model.work.cs;

import com.beautysight.liurushi.fundamental.domain.storage.ResourceInStorage;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * 图片
 *
 * @author chenlong
 * @since 1.0
 */
public class Picture extends ContentSection {

    private ResourceInStorage resource;
    protected Date modifiedAt;

    public Picture() {
        this.resource = new ResourceInStorage();
        this.initialize();
    }

    private void initialize() {
        this.type = Type.image;
    }

    public void setKey(String key) {
        this.resource.setKey(key);
    }

    public String key() {
        return resource.getKey();
    }

    public void setHash(String hash) {
        this.resource.setHash(hash);
        this.modifiedAt = new Date();
    }

    public boolean isUploaded() {
        return (resource != null
                && StringUtils.isNotBlank(resource.getKey())
                && StringUtils.isNotBlank(resource.getHash()));
    }

    public enum Format {
        jpg, jpeg, png, bmp, webp
    }

}
