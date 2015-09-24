/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.fundamental.domain.storage;

import com.beautysight.liurushi.common.ex.IllegalParamException;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chenlong
 * @since 1.0
 */
public enum ImgThumbnailSpec {

    cm1080x, cm750x, cm720x, cm640x, cm480x, wk300x, av90x;

    private static final Map<Integer, ImgThumbnailSpec> specMap = new HashMap<>();

    static {
        specMap.put(Integer.valueOf(1080), cm1080x);
        specMap.put(Integer.valueOf(750), cm750x);
        specMap.put(Integer.valueOf(720), cm720x);
        specMap.put(Integer.valueOf(640), cm640x);
        specMap.put(Integer.valueOf(480), cm480x);

        specMap.put(Integer.valueOf(300), wk300x);
        specMap.put(Integer.valueOf(90), av90x);
    }

    public static ImgThumbnailSpec of(Integer intSpec) {
        if (intSpec == null) {
            throw new IllegalParamException("intSpec is null");
        }
        ImgThumbnailSpec spec = specMap.get(intSpec);
        if (spec == null) {
            throw new IllegalParamException("Illegal intSpec:" + intSpec);
        }
        return spec;
    }

}
