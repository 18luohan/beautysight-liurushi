/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.fundamental.domain.storage;

import com.beautysight.liurushi.common.domain.AbstractEntity;
import com.beautysight.liurushi.common.ex.IllegalParamException;
import com.beautysight.liurushi.common.utils.PreconditionUtils;
import org.apache.commons.lang3.StringUtils;
import org.mongodb.morphia.annotations.Entity;

import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

/**
 * @author chenlong
 * @since 1.0
 */
@Entity(value = "file_metadata", noClassnameStored = true)
public class FileMetadata extends AbstractEntity {

    private String key;
    private String hash;
    private Type type;
    private Boolean isDeleted = Boolean.FALSE;

    private String url;

    public FileMetadata() {
    }

    private FileMetadata(Type type, BizCategory bizCategory) {
        this.key = generateKey(bizCategory);
        this.type = type;
    }

    public static FileMetadata newFile(Type type, BizCategory bizCategory) {
        if (type == Type.image) {
            return new FileMetadata(Type.image, bizCategory);
        }
        if (type == Type.video) {
            return new FileMetadata(Type.video, bizCategory);
        }
        throw new IllegalParamException("Expected type: %s, but actual %s",
                Arrays.toString(Type.values()), type);
    }

    // TODO can remove
    public static FileMetadata newImageFile(BizCategory bizCategory) {
        return newFile(Type.image, bizCategory);
    }

    public boolean isImage() {
        return (this.type == Type.image);
    }

    public boolean isUploaded() {
        return (StringUtils.isNotBlank(this.key)
                && StringUtils.isNotBlank(this.hash));
    }

    public String key() {
        return key;
    }

    public String hash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
        this.modifiedAt = new Date();
    }

    public void delete() {
        this.isDeleted = Boolean.TRUE;
        this.modifiedAt = new Date();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void validate() {
        PreconditionUtils.checkRequired("file.key", key);
        PreconditionUtils.checkRequired("file.hash", hash);
    }

    public static String generateKey(BizCategory bizCategory) {
        return new StringBuilder(bizCategory.val).append("_")
                .append(UUID.randomUUID().toString().replaceAll("-", "")).toString();
    }

    public enum BizCategory {
        avatar("av"), work("wk"), unknown("unk");

        private final String val;

        BizCategory(String val) {
            this.val = val;
        }
    }

    public enum Type {
        image, video
    }

}
