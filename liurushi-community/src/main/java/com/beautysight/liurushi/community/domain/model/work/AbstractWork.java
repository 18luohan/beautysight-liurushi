/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.domain.model.work;

import com.beautysight.liurushi.common.domain.AbstractEntity;
import com.beautysight.liurushi.common.ex.IllegalDomainStateException;
import com.beautysight.liurushi.community.domain.model.work.picstory.Cover;
import com.beautysight.liurushi.community.domain.model.work.picstory.PictureStory;
import com.beautysight.liurushi.community.domain.model.work.present.Presentation;
import org.bson.types.ObjectId;

/**
 * Represents a work that produced by typical users, professional and occupational staff,
 * which we call UGC, PGC and OGC respectively.
 *
 * @author chenlong
 * @since 1.0
 */
public abstract class AbstractWork extends AbstractEntity {

    protected PictureStory pictureStory;
    protected Presentation presentation;
    protected ObjectId authorId;
    protected Source source;

    public AbstractWork() {
    }

    public AbstractWork(PictureStory pictureStory, Presentation presentation, Author author) {
        this.pictureStory = pictureStory;
        this.presentation = presentation;
        if (author != null) {
            this.authorId = new ObjectId(author.id);
            this.determineSourceBy(author);
        }
    }

    public String title() {
        if (pictureStory == null) {
            return null;
        }
        return pictureStory.title();
    }

    public Cover cover() {
        if (pictureStory == null) {
            return null;
        }
        return pictureStory.cover();
    }

    public PictureStory pictureStory() {
        return this.pictureStory;
    }

    public Presentation presentation() {
        return this.presentation;
    }

    public ObjectId authorId() {
        return this.authorId;
    }

    private void determineSourceBy(Author author) {
        Work.Source source;
        if (author.group == Author.Group.professional) {
            source = Work.Source.pgc;
        } else if (author.group == Author.Group.amateur) {
            source = Work.Source.ugc;
        } else {
            throw new IllegalDomainStateException("Author.group:" + author.group);
        }
        this.source = source;
    }

    /**
     * 作品来源
     */
    public enum Source {
        /*
         * pgc: Professionally-generated Content
         * ugc: User-generated Content
         */
        pgc, ugc
    }

}
