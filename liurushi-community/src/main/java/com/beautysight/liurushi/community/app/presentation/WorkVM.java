/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.app.presentation;

import com.beautysight.liurushi.common.app.ViewModel;
import com.beautysight.liurushi.community.app.dpo.PictureStoryPayload;
import com.beautysight.liurushi.community.app.dpo.PresentationPayload;
import com.beautysight.liurushi.community.domain.work.Author;
import com.beautysight.liurushi.community.domain.work.Work;

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
    private Author author;

    public static WorkVM from(Work work, Map<String, String> keyToDownloadUrlMapping) {
        return from(work, keyToDownloadUrlMapping, null, null, null);
    }

    public static WorkVM from(Work work, Map<String, String> keyToDownloadUrlMapping, Author author) {
        return from(work, keyToDownloadUrlMapping, null, null, author);
    }

    public static WorkVM from(Work work, Map<String, String> keyToDownloadUrlMapping, Boolean isLiked, Boolean isFavored, Author author) {
        WorkVM workVM = new WorkVM();
        workVM.id = work.idStr();
        workVM.title = work.title();
        workVM.subtitle = work.subtitle();
        workVM.author = author;

        if (work.stats() != null) {
            workVM.stats = work.stats();
        }

        if (work.pictureStory() != null) {
            workVM.pictureStory = PictureStoryPayload.from(work.pictureStory(), keyToDownloadUrlMapping);
        }

        if (work.presentation() != null) {
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