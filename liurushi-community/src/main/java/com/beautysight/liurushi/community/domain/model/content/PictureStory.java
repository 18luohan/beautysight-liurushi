/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.domain.model.content;

import com.beautysight.liurushi.common.domain.Location;
import com.beautysight.liurushi.common.domain.ValueObject;
import com.beautysight.liurushi.common.utils.PreconditionUtils;
import com.beautysight.liurushi.identityaccess.domain.model.User;
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
public class PictureStory extends Content<PictureStory.Shot> {

    private Title title;
    private Title subtitle;
    private Layout layout;
    private Cover cover;
    private List<Shot> shots;
    private User.UserLite author;

    public PictureStory() {
    }

    public PictureStory(Title title, Title subtitle, Layout layout, Cover cover, List<Shot> shots, User.UserLite author) {
        this.title = title;
        this.subtitle = subtitle;
        this.layout = layout;
        this.cover = cover;
        this.shots = shots;
        this.author = author;
    }

    @Override
    public List<Shot> componentParts() {
        return shots;
    }

    public Cover cover() {
        return cover;
    }

    public static class Title extends ValueObject {
        private String text;
        private Location location;

        public void validate() {
            PreconditionUtils.checkRequired("Title.text", text);
            PreconditionUtils.checkRequired("Title.location", location);
            location.validate();
        }
    }

    public static class Layout extends ValueObject {
        private int rows;
        private int cols;

        public void validate() {
            PreconditionUtils.checkGreaterThanZero("Layout.rows", rows);
            PreconditionUtils.checkGreaterThanZero("Layout.cols", cols);
        }
    }

    public static class LayoutLocation extends ValueObject {
        private int whichRow;
        private int whichCol;
        private int rows;
        private int cols;

        public void validate() {
            PreconditionUtils.checkGreaterThanZero("LayoutLocation.whichRow", whichRow);
            PreconditionUtils.checkGreaterThanZero("LayoutLocation.whichCol", whichCol);
            PreconditionUtils.checkGreaterThanZero("LayoutLocation.rows", rows);
            PreconditionUtils.checkGreaterThanZero("LayoutLocation.cols", cols);
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
    public static class Shot extends ComponentPart {

        private LayoutLocation layoutLoc;
        private VisibleArea visibleArea;
    }

    public static class Cover {

        @Reference(value = "sectionId", idOnly = true)
        private ContentSection content;
        private int whPercentage;
        private VisibleArea visibleArea;

        public void setContentSection(ContentSection content) {
            this.content = content;
        }
    }

}
