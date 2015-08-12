/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.app.presentation;

import com.beautysight.liurushi.common.app.PresentationModel;
import com.beautysight.liurushi.community.domain.model.work.cs.ContentSection;
import com.beautysight.liurushi.community.domain.model.work.cs.Picture;
import com.beautysight.liurushi.fundamental.app.FileMetadataDPO;
import com.beautysight.liurushi.fundamental.domain.storage.FileMetadata;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chenlong
 * @since 1.0
 */
public class PublishWorkPresentation implements PresentationModel {

    private String workId;
    private String uploadToken;
    private Map<String, FileMetadataDPO> filesMap;

    public PublishWorkPresentation(String workId, String uploadToken, Map<String, ContentSection> contentSections) {
        this.workId = workId;
        this.uploadToken = uploadToken;
        this.filesMap = transformToFilesMap(contentSections);
    }

    private Map<String, FileMetadataDPO> transformToFilesMap(Map<String, ContentSection> contentSections) {
        Map<String, FileMetadataDPO> filesMap = new HashMap<>();
        for (Map.Entry<String, ContentSection> entry : contentSections.entrySet()) {
            if (!(entry.getValue() instanceof Picture)) {
                continue;
            }

            FileMetadata file = ((Picture) entry.getValue()).file();
            FileMetadataDPO fileDPO = new FileMetadataDPO(file.idAsStr(), file.key());
            filesMap.put(entry.getKey(), fileDPO);
        }
        return filesMap;
    }

}