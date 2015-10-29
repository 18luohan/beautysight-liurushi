/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.domain.work;

import com.google.common.collect.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;

import static com.beautysight.liurushi.community.domain.work.ContentType.*;
import static org.junit.Assert.assertEquals;

/**
 * @author chenlong
 * @since 1.0
 */
@RunWith(JUnit4.class)
public class ContentTypeTest {

    @Test
    public void testTransformToInt() throws Exception {
        List<ContentType> types = Lists.newArrayList(
                video,
                text,
                image);
        assertEquals((4 + 2 + 1), transformToInt(types));

        types = Lists.newArrayList(
                gif,
                text);
        assertEquals((8 + 1), transformToInt(types));

        types = Lists.newArrayList(
                video,
                text);
        assertEquals((4 + 1), transformToInt(types));

        types = Lists.newArrayList(
                image,
                text);
        assertEquals((2 + 1), transformToInt(types));

        types = Lists.newArrayList(
                video,
                image,
                text,
                gif);
        assertEquals((8 + 4 + 2 + 1), transformToInt(types));
    }

}