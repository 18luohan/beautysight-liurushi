/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.domain.work;

import com.beautysight.liurushi.community.domain.work.cs.ContentSection;
import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.List;

/**
 * @author chenlong
 * @since 1.0
 */
public class ContentTypes {

    public static int transformToInt(List<ContentSection.Type> types) {
        Collections.sort(types);
        int result = 0;
        for (ContentSection.Type type : types) {
            result = result | (1 << type.ordinal());
        }
        return result;
    }

    public static void main(String[] args) {
        List<ContentSection.Type> types = Lists.newArrayList(
                ContentSection.Type.video,
                ContentSection.Type.text,
                ContentSection.Type.image);
        System.out.println(ContentTypes.transformToInt(types));

        types = Lists.newArrayList(
                ContentSection.Type.gif,
                ContentSection.Type.text);
        System.out.println(ContentTypes.transformToInt(types));

        types = Lists.newArrayList(
                ContentSection.Type.video,
                ContentSection.Type.text);
        System.out.println(ContentTypes.transformToInt(types));

        types = Lists.newArrayList(
                ContentSection.Type.image,
                ContentSection.Type.text);
        System.out.println(ContentTypes.transformToInt(types));

        types = Lists.newArrayList(
                ContentSection.Type.video,
                ContentSection.Type.image,
                ContentSection.Type.text,
                ContentSection.Type.gif);
        System.out.println(ContentTypes.transformToInt(types));
    }

}