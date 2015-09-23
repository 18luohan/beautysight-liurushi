/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.domain.work;

import com.beautysight.liurushi.common.domain.AbstractEntity;
import com.beautysight.liurushi.common.ex.IllegalDomainStateException;
import com.beautysight.liurushi.community.domain.work.picstory.Cover;
import com.beautysight.liurushi.community.domain.work.picstory.PictureStory;
import com.beautysight.liurushi.community.domain.work.present.Presentation;
import org.bson.types.ObjectId;

/**
 * Represents a work that produced by typical users, professional and occupational staff,
 * which we call UGC, PGC and OGC respectively.
 *
 * @author chenlong
 * @since 1.0
 */
public abstract class AbstractWork extends AbstractEntity {

    protected String title;
    protected String subtitle;
    protected PictureStory pictureStory;
    protected Presentation presentation;
    protected ObjectId authorId;
    protected Source source;

    public AbstractWork() {
    }

    public AbstractWork(String title, String subtitle, PictureStory pictureStory, Presentation presentation, Author author) {
        this.title = title;
        this.subtitle = subtitle;
        this.pictureStory = pictureStory;
        this.presentation = presentation;
        if (author != null) {
            this.authorId = new ObjectId(author.id);
            this.determineSourceBy(author);
        }
    }

    public String title() {
        return this.title;
    }

    public String subtitle() {
        return this.subtitle;
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

    public String authorId() {
        return this.authorId.toHexString();
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
