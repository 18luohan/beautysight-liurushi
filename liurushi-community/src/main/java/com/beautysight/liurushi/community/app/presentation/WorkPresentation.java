/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.app.presentation;

import com.beautysight.liurushi.common.app.ViewModel;
import com.beautysight.liurushi.community.app.dpo.PictureStoryPayload;
import com.beautysight.liurushi.community.app.dpo.PresentationPayload;
import com.beautysight.liurushi.community.domain.model.work.picstory.PictureStory;
import com.beautysight.liurushi.community.domain.model.work.present.Presentation;

import java.util.Map;

/**
 * @author chenlong
 * @since 1.0
 */
public class WorkPresentation implements ViewModel {

    private PictureStoryPayload pictureStory;
    private PresentationPayload presentation;

    public static WorkPresentation from(PictureStory pictureStory,
                                        Presentation presentation,
                                        Map<String, String> keyToDownloadUrlMapping) {
        WorkPresentation workPresentation = new WorkPresentation();
        workPresentation.pictureStory = PictureStoryPayload.from(pictureStory, keyToDownloadUrlMapping);
        workPresentation.presentation = PresentationPayload.from(presentation, keyToDownloadUrlMapping);
        return workPresentation;
    }

}
