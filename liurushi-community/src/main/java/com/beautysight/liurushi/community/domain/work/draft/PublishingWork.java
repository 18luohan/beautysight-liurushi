/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.domain.work.draft;

import com.beautysight.liurushi.community.domain.work.AbstractWork;
import com.beautysight.liurushi.community.domain.work.Author;
import com.beautysight.liurushi.community.domain.work.Work;
import com.beautysight.liurushi.community.domain.work.picstory.Story;
import com.beautysight.liurushi.community.domain.work.present.Presentation;
import com.beautysight.liurushi.fundamental.domain.storage.FileMetadata;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Reference;

import java.util.List;

/**
 * @author chenlong
 * @since 1.0
 */
@Entity(value = "publishing_works", noClassnameStored = true)
public class PublishingWork extends AbstractWork {

    private State state = State.publishing;
    @Reference(lazy = true, idOnly = true)
    private List<FileMetadata> files;

    public PublishingWork() {
        this.initialize();
    }

    public PublishingWork(String title, String subtitle, Story Story, Presentation presentation, Author author, List<FileMetadata> files) {
        super(title, subtitle, Story, presentation, author);
        this.initialize();
        this.files = files;
    }

    public List<FileMetadata> files() {
        return this.files;
    }

    public Work transformToWork() {
        Work theWork = new Work(this.id, this.title, this.subtitle,
                this.story, this.presentation, this.authorId, this.source);
        theWork.setPublishedAt(this.createdAt);
        theWork.setContentTypes(this.contentTypes);
        return theWork;
    }

    private void initialize() {
        this.state = State.publishing;
    }

    public enum State {
        publishing
    }

}