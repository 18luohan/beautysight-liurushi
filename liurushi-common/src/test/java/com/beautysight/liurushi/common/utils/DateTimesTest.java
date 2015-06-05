/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.common.utils;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Date;

/**
 * Here is Javadoc.
 * <p/>
 * Created by chenlong on 2015-05-28.
 *
 * @author chenlong
 * @since 1.0
 */
@RunWith(JUnit4.class)
public class DateTimesTest {

    @Test
    public void afterNow() {
        DateTime dateTime = new DateTime(System.currentTimeMillis());
        boolean result = DateTimes.beforeOrEqualNow(dateTime.toDate(), 3600);
        Assert.assertFalse(result);
    }

    @Test
    public void beforeNow() {
        DateTime dateTime = new DateTime(System.currentTimeMillis());
        boolean result = DateTimes.beforeOrEqualNow(dateTime.toDate(), -3600);
        Assert.assertTrue(result);
    }

    @Test
    public void equalNow() {
        boolean result = DateTimes.beforeOrEqualNow(new Date(), 0);
        Assert.assertTrue(result);
    }

}