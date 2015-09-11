/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.app;

import com.beautysight.liurushi.common.app.ViewModel;
import com.beautysight.liurushi.community.domain.model.work.Author;
import com.beautysight.liurushi.community.domain.model.work.Work;

import java.util.Date;

/**
 * 作品概略
 *
 * @author chenlong
 * @since 1.0
 */
public class WorkProfileVM implements ViewModel {

    private String id;
    private String title;
    private String subtitle;
    public Work.Stats stats = new Work.Stats();

    private String coverPictureUrl;
    private Author author;
    private Date publishedAt;

    private Boolean isLiked = Boolean.FALSE;
    private Boolean isFavored = Boolean.FALSE;

    public WorkProfileVM(Work work, String coverPictureUrl) {
        this(work, coverPictureUrl, null);
    }

    public WorkProfileVM(Work work, String coverPictureUrl, Author author) {
        this.id = work.idAsStr();
        this.title = work.title();
        this.subtitle = work.subtitle();
        this.publishedAt = work.publishedAt();

        if (work.stats() != null) {
            this.stats = work.stats();
        }

        this.coverPictureUrl = coverPictureUrl;
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