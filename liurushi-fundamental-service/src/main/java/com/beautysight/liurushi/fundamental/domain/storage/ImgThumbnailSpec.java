/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.fundamental.domain.storage;

import com.beautysight.liurushi.common.ex.ApplicationException;
import com.beautysight.liurushi.common.ex.IllegalParamException;
import com.beautysight.liurushi.fundamental.domain.appconfig.ImageFitDeviceStrategy;
import com.google.common.collect.Lists;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chenlong
 * @since 1.0
 */
public enum ImgThumbnailSpec {

    cm1080x, cm750x, cm720x, cm640x, cm480x, wk300x, av90x;

    private static final Map<Integer, ImgThumbnailSpec> specMap = new HashMap<>();
    private static final List<Integer> specs = Lists.newArrayList(480, 640, 720, 750, 1080);

    static {
        specMap.put(Integer.valueOf(480), cm480x);
        specMap.put(Integer.valueOf(640), cm640x);
        specMap.put(Integer.valueOf(720), cm720x);
        specMap.put(Integer.valueOf(750), cm750x);
        specMap.put(Integer.valueOf(1080), cm1080x);
    }

    public static ImgThumbnailSpec bestImageSpecFor(Integer expectedSpec) {
        if (expectedSpec == null) {
            throw new IllegalParamException("intSpec is null");
        }

        Integer bestSpec = ImageFitDeviceStrategy.calculateBestImgWidthBy(expectedSpec, specs);
        return get(bestSpec);
    }

    public static ImgThumbnailSpec get(Integer intExpectedSpec) {
        ImgThumbnailSpec spec = specMap.get(intExpectedSpec);
        if (spec == null) {
            throw new ApplicationException("No corresponding thumbnail spec to %s", intExpectedSpec);
        }
        return spec;
    }

}
