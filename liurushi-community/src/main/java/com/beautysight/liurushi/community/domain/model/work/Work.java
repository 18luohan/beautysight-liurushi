/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.domain.model.work;

import com.beautysight.liurushi.common.domain.ValueObject;
import com.beautysight.liurushi.common.ex.IllegalParamException;
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

    protected Date publishedAt;
    protected Stats stats;
    protected Integer presentPriority = PresentPriority.ordinary.val();

    public Work() {
    }

    public Work(ObjectId id, String title, String subtitle, PictureStory pictureStory, Presentation presentation,
                ObjectId authorId, Source source) {
        this.id = id;
        this.title = title;
        this.subtitle = subtitle;
        this.pictureStory = pictureStory;
        this.presentation = presentation;
        this.authorId = authorId;
        this.source = source;
        this.stats = new Stats();
    }

    public Date publishedAt() {
        return this.publishedAt;
    }

    public void setPublishedAt(Date date) {
        // TODO 发表时间和创建时间有点矛盾
        this.publishedAt = date;
    }

    public Stats stats() {
        return this.stats;
    }

    public static class Stats extends ValueObject {

        private static final int DEFAULT_VAL = 0;

        private Integer viewTimes = DEFAULT_VAL;
        private Integer likeTimes = DEFAULT_VAL;
        private Integer favoriteTimes = DEFAULT_VAL;
        private Integer commentTimes = DEFAULT_VAL;

        public Integer getViewTimes() {
            return viewTimes;
        }

        public Integer getLikeTimes() {
            return likeTimes;
        }

        public Integer getFavoriteTimes() {
            return favoriteTimes;
        }

        public Integer getCommentTimes() {
            return commentTimes;
        }

    }

    public enum PresentPriority {
        selected(10), ordinary(1);

        private int val;

        PresentPriority(int val) {
            this.val = val;
        }

        public int val() {
            return this.val;
        }

        public static PresentPriority of(String priority) {
            try {
                return PresentPriority.valueOf(priority);
            } catch (IllegalArgumentException e) {
                throw new IllegalParamException(e.getMessage());
            }
        }
    }

}