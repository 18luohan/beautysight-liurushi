/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.domain.model.work.picstory;

import com.beautysight.liurushi.community.domain.model.work.WorkPart;
import com.beautysight.liurushi.community.domain.model.work.layout.Layout;

import java.util.List;

/**
 * 以图文混排方式讲述的故事。
 *
 * @author chenlong
 * @since 1.0
 */
public class PictureStory extends WorkPart<Shot> {

    private String title;
    private String subtitle;
    private Layout layout;
    private Cover cover;
    private List<Shot> shots;

    public PictureStory() {
    }

    public PictureStory(String title, String subtitle, Layout layout, Cover cover, List<Shot> shots) {
        this.title = title;
        this.subtitle = subtitle;
        this.layout = layout;
        this.cover = cover;
        this.shots = shots;
    }

    @Override
    public List<Shot> controls() {
        return shots;
    }

    public Cover cover() {
        return cover;
    }

    public String title() {
        return this.title;
    }

    public Layout layout() {
        return this.layout;
    }

}