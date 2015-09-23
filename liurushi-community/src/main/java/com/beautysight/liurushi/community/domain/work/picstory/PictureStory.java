/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.domain.work.picstory;

import com.beautysight.liurushi.community.domain.work.WorkPart;
import com.beautysight.liurushi.community.domain.work.layout.Layout;

import java.util.List;

/**
 * 以图文混排方式讲述的故事。
 *
 * @author chenlong
 * @since 1.0
 */
public class PictureStory extends WorkPart<Shot> {

    private Layout layout;
    private Cover cover;
    private List<Shot> shots;

    public PictureStory() {
    }

    public PictureStory(Layout layout, Cover cover, List<Shot> shots) {
        this.layout = layout;
        this.cover = cover;
        this.shots = shots;
    }

    @Override
    public List<Shot> controls() {
        return shots;
    }

    public void sliceShots(int fromIndex, int toIndex) {
        this.shots = this.shots.subList(fromIndex, toIndex);
    }

    public Cover cover() {
        return cover;
    }

    public Layout layout() {
        return this.layout;
    }

}