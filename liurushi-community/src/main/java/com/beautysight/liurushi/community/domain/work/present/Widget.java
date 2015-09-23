/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.domain.work.present;

import com.beautysight.liurushi.common.domain.Location;
import com.beautysight.liurushi.common.domain.ValueObject;
import com.beautysight.liurushi.common.utils.PreconditionUtils;

/**
 * Text widget
 */
public class Widget extends ValueObject {

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
