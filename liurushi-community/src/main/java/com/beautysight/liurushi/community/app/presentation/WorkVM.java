/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.app.presentation;

import com.beautysight.liurushi.common.app.ViewModel;
import com.beautysight.liurushi.community.app.dpo.PictureStoryPayload;
import com.beautysight.liurushi.community.app.dpo.PresentationPayload;
import com.beautysight.liurushi.community.domain.model.work.Work;

import java.util.Map;

/**
 * @author chenlong
 * @since 1.0
 */
public class WorkVM implements ViewModel {

    public String id;
    public String title;
    public String subtitle;
    public Boolean isLiked = Boolean.FALSE;
    public Boolean isFavored = Boolean.FALSE;
    public Work.Stats stats = new Work.Stats();
    private PictureStoryPayload pictureStory;
    private PresentationPayload presentation;

    public static WorkVM from(Work work, Map<String, String> keyToDownloadUrlMapping) {
        return from(work, keyToDownloadUrlMapping, null, null);
    }

    public static WorkVM from(Work work, Map<String, String> keyToDownloadUrlMapping, Boolean isLiked, Boolean isFavored) {
        WorkVM workVM = new WorkVM();
        workVM.id = work.idAsStr();
        workVM.title = work.title();
        workVM.subtitle = work.subtitle();
        workVM.pictureStory = PictureStoryPayload.from(work.pictureStory(), keyToDownloadUrlMapping);

        if (work.stats() != null) {
            workVM.stats = work.stats();
        }

        if (workVM.presentation != null) {
            workVM.presentation = PresentationPayload.from(work.presentation(), keyToDownloadUrlMapping);
        }

        if (isLiked != null) {
            workVM.isLiked = isLiked;
        }
        if (isFavored != null) {
            workVM.isFavored = isFavored;
        }

        return workVM;
    }

}