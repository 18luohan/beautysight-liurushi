/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.domain.work;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author chenlong
 * @since 1.0
 */
public enum ContentType {

    text(1), image(2), video(3), gif(4);

    /**
     * order就是移动app开发过程中支持具体内容类型的顺序。
     * 这里之所以不使用枚举类的ordinal()，是因为一旦不小心改变枚举列表的顺序，
     * ordinal也相应改变了，这会导致transformToInt()方法计算的结果不正确，
     * 进而导致查询有bug。
     */
    private int order;

    ContentType(int order) {
        this.order = order;
    }

    private static final Comparator<ContentType> comparator = new Comparator<ContentType>() {
        @Override
        public int compare(ContentType o1, ContentType o2) {
            return (o1.order - o2.order);
        }
    };

    public static int transformToInt(List<ContentType> types) {
        Collections.sort(types, comparator);
        int result = 0;
        for (ContentType type : types) {
            result = result | (1 << type.ordinal());
        }
        return result;
    }

}
