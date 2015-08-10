/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.domain.model.work.present;

import com.beautysight.liurushi.community.domain.model.work.WorkPart;

import java.util.List;

/**
 * 像幻灯片那样的演示
 *
 * @author chenlong
 * @since 1.0
 */
public class Presentation extends WorkPart<Slide> {

    private List<Slide> slides;

    private Presentation() {
    }

    public Presentation(List<Slide> slides) {
        this.slides = slides;
    }

    @Override
    public List<Slide> controls() {
        return slides;
    }

}
