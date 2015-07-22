/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.domain.model.content;

import com.beautysight.liurushi.common.domain.Location;
import com.beautysight.liurushi.common.domain.ValueObject;
import com.beautysight.liurushi.common.utils.PreconditionUtils;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Reference;

import java.util.List;

/**
 * 以图文混排方式讲述的故事。
 *
 * @author chenlong
 * @since 1.0
 */
@Entity(value = "picture_stories", noClassnameStored = true)
public class PictureStory extends Work<PictureStory.Shot> {

    private String title;
    private String subtitle;
    private Layout layout;
    private Cover cover;
    private List<Shot> shots;

    public PictureStory() {
    }

    public PictureStory(String title, String subtitle, Layout layout, Cover cover, List<Shot> shots, Author author, Source source) {
        super(author, source);
        this.title = title;
        this.subtitle = subtitle;
        this.layout = layout;
        this.cover = cover;
        this.shots = shots;
    }

    @Override
    public List<Shot> componentParts() {
        return shots;
    }

    public Cover cover() {
        return cover;
    }

    public static class Layout extends ValueObject {
        private Integer rows;
        private Integer cols;

        public void validate() {
            PreconditionUtils.checkGreaterThanZero("Layout.rows", rows);
            PreconditionUtils.checkGreaterThanZero("Layout.cols", cols);
        }
    }

    public static class ControlSize extends ValueObject {
        private Integer rowSpan;
        private Integer colSpan;

        public void validate() {
            PreconditionUtils.checkGreaterThanZero("ControlSize(e.g. shot).rowSpan", rowSpan);
            PreconditionUtils.checkGreaterThanZero("ControlSize(e.g. shot).colSpan", colSpan);
        }
    }

    public static class VisibleArea extends ValueObject {
        private Location startLoc;
        private Location endLoc;

        public void validate() {
            PreconditionUtils.checkRequired("VisibleArea.startLoc", startLoc);
            PreconditionUtils.checkRequired("VisibleArea.endLoc", endLoc);
            startLoc.validate();
            endLoc.validate();
        }
    }

    /**
     * 一张照片，或叫一个镜头。镜头里面可以画面，也可以是文字。
     */
    public static class Shot extends Control {
        private ControlSize size;
        private VisibleArea visibleArea;
    }

    public static class Cover {

        @Reference(value = "sectionId", idOnly = true)
        private Picture picture;

        /**
         * 宽高比
         */
        private Integer whPercentage;
        private VisibleArea visibleArea;

        public void setPicture(Picture picture) {
            this.picture = picture;
        }

        public String pictureKey() {
            return this.picture.key();
        }

    }

}
