/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.domain.work.cs;

import com.beautysight.liurushi.fundamental.domain.storage.FileMetadata;
import org.mongodb.morphia.annotations.Reference;

/**
 * @author chenlong
 * @since 1.0
 */
public abstract class Rich extends ContentSection {

    @Reference(value = "fileId", idOnly = true)
    private FileMetadata file;

    public void setFile(FileMetadata file) {
        this.file = file;
    }

    public FileMetadata file() {
        return this.file;
    }

    public String fileKey() {
        return file.key();
    }

}