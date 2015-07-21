/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.domain.model.content;

import com.beautysight.liurushi.common.domain.AbstractEntity;
import org.bson.types.ObjectId;

import java.util.Date;
import java.util.List;

/**
 * Represents a work that produced by typical users, professional and occupational staff,
 * which we call UGC, PGC and OGC respectively.
 *
 * @author chenlong
 * @since 1.0
 */
public abstract class Work<T extends Control> extends AbstractEntity {

    private ObjectId authorId;
    private Date publishedAt;
    private Source source;

    public Work() {
        this(null);
    }

    public Work(Author author) {
        if (author != null) {
            this.authorId = new ObjectId(author.id);
        }
        this.publishedAt = new Date();
    }

    public ObjectId authorId() {
        return this.authorId;
    }

    public abstract List<T> componentParts();

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
