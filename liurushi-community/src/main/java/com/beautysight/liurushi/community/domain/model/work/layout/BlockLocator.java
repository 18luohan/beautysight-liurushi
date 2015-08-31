/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.domain.model.work.layout;

import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 布局中
 *
 * @author chenlong
 * @since 1.0
 */
public final class BlockLocator {

    private int colCount;
    private List<FreeSpace> freeSpaces;
    private static final FreeSpace.FreeSpaceComparator comparator = new FreeSpace.FreeSpaceComparator();

    public BlockLocator(int colCount) {
        Preconditions.checkArgument((colCount > 0), "layout cols count must be greater than 0");
        this.colCount = colCount;
        initFreePlaces();
    }

    public Block.Position locateOneBlockBy(Block.Size blockSize) {
        Block.Position blockPosition = doLocateOneBlock(blockSize);
        reCalcFreePlaces(new Block(blockPosition, blockSize));
        return blockPosition;
    }

    private void initFreePlaces() {
        freeSpaces = new ArrayList<>();
        // 初始时整个布局就是一个可用空间
        freeSpaces.add(new FreeSpace(0, 0, colCount));
    }

    private Block.Position doLocateOneBlock(Block.Size blockSize) {
        Block.Position position = null;
        if (freeSpaces != null && freeSpaces.size() > 0) {
            for (FreeSpace free : freeSpaces) {
                if (blockSize.colSpan() <= free.colSpan()) {
                    position = new Block.Position(free.colStart(), free.rowStart());
                    break;
                }
            }
        }

        return position;
    }

    private void reCalcFreePlaces(Block cell) {
        List<FreeSpace> freePlacesToAdd = new ArrayList<FreeSpace>();
        List<FreeSpace> freePlacesToDelete = new ArrayList<FreeSpace>();

        for (int i = 0; i < freeSpaces.size(); i++) {
            FreeSpace freeSpace = freeSpaces.get(i);

            if (freeSpace.canContain(cell)) {

                freePlacesToDelete.add(freeSpace);
                freePlacesToAdd.addAll(freeSpace.producedFreeSpacesWhenContain(cell));

            } else if (freeSpace.intersectWithVertically(cell)) {

                freePlacesToDelete.add(freeSpace);
                freePlacesToAdd.addAll(freeSpace.producedFreeSpacesWhenIntersectWithVertically(cell));

            } else if (freeSpace.intersectWithFromLeftHorizontally(cell)) {

                freePlacesToDelete.add(freeSpace);
                freePlacesToAdd.addAll(freeSpace.producedFreeSpacesWhenIntersectWithFromLeftHorizontally(cell));

            } else if (freeSpace.intersectWithFromRightHorizontally(cell)) {

                freePlacesToDelete.add(freeSpace);
                freePlacesToAdd.addAll(freeSpace.producedFreeSpacesWhenIntersectWithFromRightHorizontally(cell));

            }
        }

        for (FreeSpace freePlace : freePlacesToDelete) {
            freeSpaces.remove(freePlace);
        }

        Collections.sort(freePlacesToAdd, comparator);
        for (FreeSpace freePlace : freePlacesToAdd) {
            if (freePlace.colSpan() == 0 || isFreeSpaceToAddContained(freePlace)) {
                continue;
            }
            freeSpaces.add(freePlace);
        }

        Collections.sort(freeSpaces, comparator);
    }

    private boolean isFreeSpaceToAddContained(FreeSpace freeSpaceToAdd) {
        for (FreeSpace freeSpace : freeSpaces) {
            if (freeSpace.containOrSameAs(freeSpaceToAdd)) {
                return true;
            }
        }
        return false;
    }

}
