/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.app;

import com.beautysight.liurushi.community.domain.work.cs.ContentSection;

/**
 * @author chenlong
 * @since 1.0
 */
public class WorkProfileVMV10 extends WorkProfileVM {

    private String coverPictureUrl;

    public WorkProfileVMV10(WorkProfileVM workProfileVM) {
        this.id = workProfileVM.id;
        this.title = workProfileVM.title;
        this.subtitle = workProfileVM.subtitle;
        this.stats = workProfileVM.stats;
        this.author = workProfileVM.author;
        this.publishedAt = workProfileVM.publishedAt;
        this.isLiked = workProfileVM.isLiked;
        this.isFavored = workProfileVM.isFavored;

        if (workProfileVM.cover.type == ContentSection.Type.image) {
            this.coverPictureUrl = workProfileVM.cover.fileUrl;
        }
    }

}
