/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.app.presentation;

import com.beautysight.liurushi.common.app.DPO;
import com.beautysight.liurushi.common.app.PresentationModel;
import com.beautysight.liurushi.community.domain.model.work.cs.ContentSection;
import com.beautysight.liurushi.community.domain.model.work.cs.Picture;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chenlong
 * @since 1.0
 */
public class PublishWorkPresentation implements PresentationModel {

    private String workId;
    private String uploadToken;
    private Map<String, PictureDPO> picturesMap;

    public PublishWorkPresentation(String workId, String uploadToken, Map<String, ContentSection> contentSections) {
        this.workId = workId;
        this.uploadToken = uploadToken;
        this.picturesMap = transformToPicturesMap(contentSections);
    }

    private Map<String, PictureDPO> transformToPicturesMap(Map<String, ContentSection> contentSections) {
        Map<String, PictureDPO> picturesMap = new HashMap<>();
        for (Map.Entry<String, ContentSection> entry : contentSections.entrySet()) {
            if (!(entry.getValue() instanceof Picture)) {
                continue;
            }

            Picture pic = (Picture) entry.getValue();
            PictureDPO picDPO = new PictureDPO();
            picDPO.id = pic.idAsStr();
            picDPO.key = pic.key();
            picturesMap.put(entry.getKey(), picDPO);
        }
        return picturesMap;
    }

    private static class PictureDPO extends DPO {
        private String id;
        private String key;
    }

}