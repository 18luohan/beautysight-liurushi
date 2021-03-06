/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.app.presentation;

import com.beautysight.liurushi.common.app.ViewModel;
import com.beautysight.liurushi.community.domain.work.cs.ContentSection;
import com.beautysight.liurushi.community.domain.work.cs.Rich;
import com.beautysight.liurushi.fundamental.app.FileMetadataPayload;
import com.beautysight.liurushi.fundamental.domain.storage.FileMetadata;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chenlong
 * @since 1.0
 */
public class PublishWorkPresentation implements ViewModel {

    private String workId;
    private Map<String, FileMetadataPayload> filesMap;

    public PublishWorkPresentation(String workId, Map<String, ContentSection> contentSections) {
        this.workId = workId;
        this.filesMap = transformToFilesMap(contentSections);
    }

    private Map<String, FileMetadataPayload> transformToFilesMap(Map<String, ContentSection> contentSections) {
        Map<String, FileMetadataPayload> filesMap = new HashMap<>();
        for (Map.Entry<String, ContentSection> entry : contentSections.entrySet()) {
            if (!(entry.getValue() instanceof Rich)) {
                continue;
            }

            FileMetadata file = ((Rich) entry.getValue()).file();
            FileMetadataPayload fileDPO = new FileMetadataPayload(file.idStr(), file.key(), file.getUploadToken());
            filesMap.put(entry.getKey(), fileDPO);
        }
        return filesMap;
    }

}