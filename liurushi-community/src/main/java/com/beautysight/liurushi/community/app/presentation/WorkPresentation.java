/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.app.presentation;

import com.beautysight.liurushi.common.app.PresentationModel;
import com.beautysight.liurushi.community.app.dpo.PictureStoryDPO;
import com.beautysight.liurushi.community.app.dpo.PresentationDPO;
import com.beautysight.liurushi.community.domain.model.work.picstory.PictureStory;
import com.beautysight.liurushi.community.domain.model.work.present.Presentation;

import java.util.Map;

/**
 * @author chenlong
 * @since 1.0
 */
public class WorkPresentation implements PresentationModel {

    private PictureStoryDPO pictureStory;
    private PresentationDPO presentation;

    public static WorkPresentation from(PictureStory pictureStory,
                                        Presentation presentation,
                                        Map<String, String> keyToDownloadUrlMapping) {
        WorkPresentation workPresentation = new WorkPresentation();
        workPresentation.pictureStory = PictureStoryDPO.from(pictureStory, keyToDownloadUrlMapping);
        workPresentation.presentation = PresentationDPO.from(presentation, keyToDownloadUrlMapping);
        return workPresentation;
    }

}
