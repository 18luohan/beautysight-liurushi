/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.domain.model.content;

import com.beautysight.liurushi.common.domain.Location;
import com.beautysight.liurushi.common.domain.ValueObject;
import com.beautysight.liurushi.common.utils.PreconditionUtils;
import com.beautysight.liurushi.identityaccess.domain.model.User;
import org.mongodb.morphia.annotations.Entity;

import java.util.List;

/**
 * 像幻灯片那样的演示
 *
 * @author chenlong
 * @since 1.0
 */
@Entity(value = "presentations", noClassnameStored = true)
public class Presentation extends Content<Presentation.Slide> {

    private List<Slide> slides;
    private User.UserLite author;

    private Presentation() {
    }

    public Presentation(List<Slide> slides, User.UserLite author) {
        this.slides = slides;
        this.author = author;
    }

    @Override
    public List<Slide> componentParts() {
        return slides;
    }

    /**
     * 一张幻灯片
     */
    public static class Slide extends ComponentPart {

        private Widget textWidget;

        public Slide() {
        }

    }

    /**
     * Text widget
     */
    public static class Widget extends ValueObject {

        private String text;
        private Color color;
        private Orientation orientation;
        private Location location;

        public enum Color {
            white, black, orange
        }

        public enum Orientation {
            left_sloping, right_sloping
        }

        public void validate() {
            PreconditionUtils.checkRequired("Widget.text", text);
            PreconditionUtils.checkRequired("Widget.color", color);
            PreconditionUtils.checkRequired("Widget.orientation", orientation);
            PreconditionUtils.checkRequired("Widget.location", location);
            location.validate();
        }

    }

}
