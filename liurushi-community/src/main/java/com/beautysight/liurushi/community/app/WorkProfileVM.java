/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.app;

import com.beautysight.liurushi.common.app.ViewModel;
import com.beautysight.liurushi.community.app.dpo.ContentSectionPayload;
import com.beautysight.liurushi.community.domain.work.Author;
import com.beautysight.liurushi.community.domain.work.Work;

import java.util.Date;

/**
 * 作品概略
 *
 * @author chenlong
 * @since 1.0
 */
public class WorkProfileVM implements ViewModel {

    protected String id;
    protected String title;
    protected String subtitle;
    protected Work.Stats stats = new Work.Stats();

    protected ContentSectionPayload.RichPayload cover;

    protected Author author;
    protected Date publishedAt;

    protected Boolean isLiked = Boolean.FALSE;
    protected Boolean isFavored = Boolean.FALSE;

    public WorkProfileVM() {
    }

    public WorkProfileVM(Work work, ContentSectionPayload.RichPayload coverContent) {
        this(work, coverContent, null);
    }

    public WorkProfileVM(Work work, ContentSectionPayload.RichPayload coverContent, Author author) {
        this.id = work.idStr();
        this.title = work.title();
        this.subtitle = work.subtitle();
        this.publishedAt = work.publishedAt();

        if (work.stats() != null) {
            this.stats = work.stats();
        }

        if (coverContent != null) {
            this.cover = coverContent;
        }

        this.author = author;
    }

    public void setIsLiked(Boolean isLiked) {
        if (isLiked != null) {
            this.isLiked = isLiked;
        }
    }

    public void setIsFavored(Boolean isFavored) {
        if (isFavored != null) {
            this.isFavored = isFavored;
        }
    }

}