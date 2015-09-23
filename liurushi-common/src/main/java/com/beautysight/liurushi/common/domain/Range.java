/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.common.domain;

import com.beautysight.liurushi.common.utils.PreconditionUtils;
import com.google.common.base.Optional;

/**
 * @author chenlong
 * @since 1.0
 */
public class Range {

    private Optional<String> referencePoint;
    private Integer offset;
    private OffsetDirection direction = OffsetDirection.both;

    public Range(Integer offset, OffsetDirection direction) {
        this(null, offset, direction);
    }

    public Range(String referencePoint, Integer offset, OffsetDirection direction) {
        this.setReferencePoint(referencePoint);
        this.setOffset(offset);
        this.setDirection(direction);
    }

    private void setReferencePoint(String referencePoint) {
        this.referencePoint = Optional.fromNullable(referencePoint);
    }

    private void setOffset(Integer offset) {
        PreconditionUtils.checkGreaterThanZero("offset", offset);
        this.offset = offset;
    }

    private void setDirection(OffsetDirection direction) {
        if (direction == null) {
            return;
        }
        this.direction = direction;
    }

    public Optional<String> referencePoint() {
        return this.referencePoint;
    }

    public Integer offset() {
        return this.offset;
    }

    public OffsetDirection direction() {
        return this.direction;
    }

    public boolean both() {
        return (this.direction == OffsetDirection.both);
    }

    public boolean after() {
        return (this.direction == OffsetDirection.after);
    }

    public boolean before() {
        return (this.direction == OffsetDirection.before);
    }

    /**
     * @author chenlong
     * @since 1.0
     */
    public enum OffsetDirection {
        before, after, both
    }

}
