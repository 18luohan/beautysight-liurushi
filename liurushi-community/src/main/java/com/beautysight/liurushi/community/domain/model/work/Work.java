/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.domain.model.work;

import com.beautysight.liurushi.community.domain.model.work.picstory.PictureStory;
import com.beautysight.liurushi.community.domain.model.work.present.Presentation;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;

import java.util.Date;

/**
 * 已发表的作品
 *
 * @author chenlong
 * @since 1.0
 */
@Entity(value = "works", noClassnameStored = true)
public class Work extends AbstractWork {

    private Date publishedAt;

    public Work() {
    }

    public Work(ObjectId id, PictureStory pictureStory, Presentation presentation,
                ObjectId authorId, Source source) {
        this.id = id;
        this.pictureStory = pictureStory;
        this.presentation = presentation;
        this.authorId = authorId;
        this.source = source;
    }

    public Date publishedAt() {
        return this.publishedAt;
    }

    public void setPublishedAt(Date date) {
        // TODO 发表时间和创建时间有点矛盾
        this.publishedAt = date;
    }

}