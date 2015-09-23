/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.domain.work;

import org.mongodb.morphia.annotations.Entity;

/**
 * @author chenlong
 * @since 1.0
 */
@Entity(value = "discarded_works", noClassnameStored = true)
public class DiscardedWork extends Work {

    public DiscardedWork() {
    }

    public DiscardedWork(Work work) {
        this.id = work.id();
        this.createdAt = work.createdAt();
        this.modifiedAt = work.modifiedAt();

        this.title = work.title;
        this.subtitle = work.subtitle;
        this.pictureStory = work.pictureStory;
        this.presentation = work.presentation;
        this.authorId = work.authorId;
        this.source = work.source;

        this.publishedAt = work.publishedAt;
        this.presentPriority = work.presentPriority;
        this.stats = work.stats;
    }

    public Work transformToWork() {
        Work work = new Work();

        work.setMongoId(this.id);
        work.setCreatedAt(this.createdAt);
        work.setModifiedAt(this.modifiedAt);

        work.title = this.title;
        work.subtitle = this.subtitle;
        work.pictureStory = this.pictureStory;
        work.presentation = this.presentation;
        work.authorId = this.authorId;
        work.source = this.source;

        work.publishedAt = this.publishedAt;
        work.presentPriority = this.presentPriority;
        work.stats = this.stats;
        return work;
    }

}