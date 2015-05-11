/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */
package com.beautysight.liurushi.common.utils;

import com.beautysight.liurushi.common.application.Presentation;
import org.joda.time.DateTime;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * Here is Javadoc.
 * <p/>
 * Created by chenlong on 2015-05-11.
 *
 * @author chenlong
 * @since 1.0
 */
public class JsonsTest {

    @Test
    public void testToJsonString() throws Exception {
        Post post = new Post(1000, "penny", newDate(2012, 12, 12, 16, 30, 30, 0));
        String actual = Jsons.toJsonString(post);
        String expected = "{\"threadId\":1000,\"author\":\"penny\",\"committedAt\":\"2012-12-12T16:30:30Z\"}";
        assertEquals(expected, actual);
    }

    private Date newDate(int year,
                         int monthOfYear,
                         int dayOfMonth,
                         int hourOfDay,
                         int minuteOfHour,
                         int secondOfMinute,
                         int millisOfSecond) {
        return new DateTime(year, monthOfYear, dayOfMonth, hourOfDay, minuteOfHour, secondOfMinute,
                millisOfSecond).toDate();
    }

    private static class Post implements Presentation {
        private int threadId;
        private String author;
        //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        //@JsonSerialize(using = CustomDateSerializer.class)
        private Date committedAt;

        private Post(int threadId, String author, Date aDate) {
            this.threadId = threadId;
            this.author = author;
            this.committedAt = aDate;
        }
    }

}