/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.domain.model.work.layout;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author chenlong
 * @since 1.0
 */
public final class FreeSpace {

    private Block.Position position;
    private int colSpan;

    FreeSpace(int colStart, int rowStart, int colSpan) {
        this.position = new Block.Position(colStart, rowStart);
        this.colSpan = colSpan;
    }

    public int colSpan() {
        return colSpan;
    }

    public int rowStart() {
        return position.rowIndex;
    }

    public int colStart() {
        return position.colIndex;
    }

    public int colEnd() {
        return (position.colIndex + this.colSpan);
    }

    public boolean containOrSameAs(FreeSpace that) {
        if (that.colStart() >= this.colStart()
                && that.colEnd() <= this.colEnd()
                && that.position.rowIndex >= this.rowStart()) {
            return true;
        }
        return false;
    }

    /**
     * 当前这个可用空间是否能容纳给定的块
     *
     * @param block
     * @return
     */
    public boolean canContain(Block block) {
        return (this.rowStart() == block.rowStart());
    }

    public List<FreeSpace> producedFreeSpacesWhenContain(Block block) {
        List<FreeSpace> producedFreeSpaces = new ArrayList<>(2);
        producedFreeSpaces.add(new FreeSpace(
                this.colStart(), block.rowEnd(),
                this.colSpan()));
        producedFreeSpaces.add(new FreeSpace(
                block.colEnd(), this.rowStart(),
                this.colSpan() - block.colSpan()));
        return producedFreeSpaces;
    }

    public boolean intersectWithVertically(Block block) {
        return (this.rowStart() > block.rowStart() && this.rowStart() < block.rowEnd())
                && (
                (block.colStart() >= this.colStart() && block.colStart() < this.colEnd())
                        || (block.colEnd() > this.colStart() && block.colEnd() <= this.colEnd())
        );
    }

    public List<FreeSpace> producedFreeSpacesWhenIntersectWithVertically(Block block) {
        List<FreeSpace> producedFreeSpaces = new ArrayList<FreeSpace>(3);
        producedFreeSpaces.add(new FreeSpace(this.colStart(), this.rowStart(),
                block.colStart() - this.colStart()));
        producedFreeSpaces.add(new FreeSpace(block.colEnd(), this.rowStart(),
                this.colEnd() - block.colEnd()));
        producedFreeSpaces.add(new FreeSpace(this.colStart(), block.rowEnd(),
                this.colSpan()));
        return producedFreeSpaces;
    }

    public boolean intersectWithFromLeftHorizontally(Block block) {
        return (this.colStart() >= block.colStart() && this.colStart() < block.colEnd())
                && (block.rowEnd() > this.rowStart());
    }

    public List<FreeSpace> producedFreeSpacesWhenIntersectWithFromLeftHorizontally(Block block) {
        List<FreeSpace> producedFreeSpaces = new ArrayList<FreeSpace>(1);
        if (block.colEnd() < this.colEnd()) {
            producedFreeSpaces.add(new FreeSpace(block.colEnd(), this.rowStart(),
                    (this.colEnd() - block.colEnd())));
        }
        return producedFreeSpaces;
    }

    public boolean intersectWithFromRightHorizontally(Block block) {
        return (this.colEnd() > block.colStart() && this.colEnd() <= block.colEnd())
                && (block.rowEnd() > this.rowStart());
    }

    public List<FreeSpace> producedFreeSpacesWhenIntersectWithFromRightHorizontally(Block block) {
        List<FreeSpace> producedFreeSpaces = new ArrayList<FreeSpace>(1);
        if (block.colStart() > this.colStart()) {
            producedFreeSpaces.add(new FreeSpace(this.colStart(), this.rowStart(),
                    (block.colStart() - this.colStart())));
        }
        return producedFreeSpaces;
    }

    public static class FreeSpaceComparator implements Comparator<FreeSpace> {
        @Override
        public int compare(FreeSpace o1, FreeSpace o2) {
            if (o1.rowStart() == o2.rowStart()) {
                return (o1.colStart() - o2.colStart());
            } else {
                return (o1.rowStart() - o2.rowStart());
            }
        }
    }

}
