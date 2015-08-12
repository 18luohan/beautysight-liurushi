/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.domain.model.work.draft;

import com.beautysight.liurushi.community.domain.model.work.AbstractWork;
import com.beautysight.liurushi.community.domain.model.work.Author;
import com.beautysight.liurushi.community.domain.model.work.Work;
import com.beautysight.liurushi.community.domain.model.work.picstory.PictureStory;
import com.beautysight.liurushi.community.domain.model.work.present.Presentation;
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

    public PublishingWork(PictureStory pictureStory, Presentation presentation, Author author, List<FileMetadata> files) {
        super(pictureStory, presentation, author);
        this.initialize();
        this.files = files;
    }

    public List<FileMetadata> files() {
        return this.files;
    }

    public Work transformToWork() {
        Work theWork = new Work(this.id, this.pictureStory, this.presentation, this.authorId, this.source);
        theWork.setPublishedAt(this.createdAt);
        return theWork;
    }

    private void initialize() {
        this.state = State.publishing;
    }

    public enum State {
        publishing
    }

}