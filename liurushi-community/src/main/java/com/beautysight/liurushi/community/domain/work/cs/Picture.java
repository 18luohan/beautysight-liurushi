/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.domain.work.cs;

import com.beautysight.liurushi.fundamental.domain.storage.FileMetadata;
import org.mongodb.morphia.annotations.Reference;

/**
 * 图片
 *
 * @author chenlong
 * @since 1.0
 */
public class Picture extends ContentSection {

    @Reference(value = "fileId", idOnly = true)
    private FileMetadata file;

    public Picture() {
        this.type = Type.image;
    }

    public void setFile(FileMetadata file) {
        this.file = file;
    }

    public FileMetadata file() {
        return this.file;
    }

    public String key() {
        return file.key();
    }

    public enum Format {
        jpg, jpeg, png, bmp, webp
    }

}