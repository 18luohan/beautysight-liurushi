/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.domain.work;

import com.beautysight.liurushi.common.domain.AbstractEntity;
import com.beautysight.liurushi.common.ex.IllegalDomainStateException;
import com.beautysight.liurushi.community.domain.work.picstory.Cover;
import com.beautysight.liurushi.community.domain.work.picstory.Story;
import com.beautysight.liurushi.community.domain.work.present.Presentation;
import org.bson.types.ObjectId;

import java.util.List;

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
    protected Story story;
    protected Presentation presentation;
    protected ObjectId authorId;
    protected Source source;

    protected Integer contentTypes;

    public AbstractWork() {
    }

    public AbstractWork(String title, String subtitle, Story Story, Presentation presentation, Author author) {
        this.title = title;
        this.subtitle = subtitle;
        this.story = Story;
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
        if (story == null) {
            return null;
        }
        return story.cover();
    }

    public Story story() {
        return this.story;
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

    public void setContentTypes(List<ContentType> contentTypes) {
        this.contentTypes = ContentType.transformToInt(contentTypes);
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
