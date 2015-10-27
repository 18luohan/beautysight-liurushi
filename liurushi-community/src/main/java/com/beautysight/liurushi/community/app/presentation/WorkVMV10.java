/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.app.presentation;

import com.beautysight.liurushi.community.app.dpo.StoryPayload;

/**
 * @author chenlong
 * @since 1.0
 */
public class WorkVMV10 extends WorkVM {

    public StoryPayload pictureStory;

    public WorkVMV10(WorkVM workVM) {
        this.id = workVM.id;
        this.title = workVM.title;
        this.subtitle = workVM.subtitle;

        this.isLiked = workVM.isLiked;
        this.isFavored = workVM.isFavored;

        this.stats = workVM.stats;
        this.pictureStory = workVM.story;
        this.presentation = workVM.presentation;
        this.author = workVM.author;
    }

}
