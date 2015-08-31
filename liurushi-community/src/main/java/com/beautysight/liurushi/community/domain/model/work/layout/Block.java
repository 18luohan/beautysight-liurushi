/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.domain.model.work.layout;

import com.beautysight.liurushi.common.domain.ValueObject;
import com.beautysight.liurushi.common.utils.PreconditionUtils;

/**
 * 布局中的块。
 *
 * @author chenlong
 * @since 1.0
 */
public class Block extends ValueObject {

    Position position;
    Size size;

    public Block(Position position, Size size) {
        this.position = position;
        this.size = size;
    }

    public int colStart() {
        return position.colIndex;
    }

    public int colEnd() {
        return (position.colIndex + size.colSpan);
    }

    public int rowStart() {
        return position.rowIndex;
    }

    public int rowEnd() {
        return (position.rowIndex + size.rowSpan);
    }

    public int colSpan() {
        return size.colSpan;
    }

    /**
     * 块在布局中的大小
     */
    public static class Size extends ValueObject {
        private Integer colSpan;
        private Integer rowSpan;

        public Size() {
        }

        public Size(Integer colSpan, Integer rowSpan) {
            this.colSpan = colSpan;
            this.rowSpan = rowSpan;
        }

        public Integer colSpan() {
            return this.colSpan;
        }

        public void validate() {
            PreconditionUtils.checkGreaterThanZero("ControlSize(e.g. shot).rowSpan", rowSpan);
            PreconditionUtils.checkGreaterThanZero("ControlSize(e.g. shot).colSpan", colSpan);
        }
    }

    /**
     * 块在布局中的位置
     */
    public static class Position extends ValueObject {
        int colIndex;
        int rowIndex;

        public Position() {
        }

        public Position(Integer colIndex, Integer rowIndex) {
            this.colIndex = colIndex;
            this.rowIndex = rowIndex;
        }

        public void validate() {
            PreconditionUtils.checkGreaterThanOrEqZero("LayoutPosition.colIndex", colIndex);
            PreconditionUtils.checkGreaterThanOrEqZero("LayoutPosition.rowIndex", rowIndex);
        }
    }

}
