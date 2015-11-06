/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.fundamental.app;

import com.beautysight.liurushi.common.app.ViewModel;
import com.beautysight.liurushi.fundamental.domain.storage.FileMetadata;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chenlong
 * @since 1.0
 */
public class PrepareForUploadPresentation implements ViewModel {

    private List<FileMetadataPayload> files;

    public PrepareForUploadPresentation(List<FileMetadata> filesMetadata) {
        List<FileMetadataPayload> files = new ArrayList<>();
        for (FileMetadata metadata : filesMetadata) {
            files.add(new FileMetadataPayload(metadata.idStr(), metadata.key(), metadata.getUploadToken()));
        }
        this.files = files;
    }

}