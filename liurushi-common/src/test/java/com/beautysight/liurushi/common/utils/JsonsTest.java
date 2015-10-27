/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */
package com.beautysight.liurushi.common.utils;

import com.beautysight.liurushi.common.app.ViewModel;
import org.joda.time.DateTime;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * @author chenlong
 * @since 1.0
 */
public class JsonsTest {

    @Test
    public void toJsonString() throws Exception {
        Post post = new Post(1000, "penny", newDate(2012, 12, 12, 16, 30, 30, 0));
        String actual = Jsons.toJsonString(post);
        String expected = "{\"threadId\":1000,\"author\":\"penny\",\"committedAt\":\"2012-12-12T16:30:30Z\"}";
        assertEquals(expected, actual);
    }

    /**
     * 测试反序列化时遇到不认识的属性，json引擎是否会抛出异常。期望不要抛出异常，忽略即可。
     */
    @Test
    public void toObjectWithUnknownProperties() {
        String jsonString = "{\"threadId\":1000,\"author\":\"penny\",\"committedAt\":\"2012-12-12T16:30:30Z\"}";
        Jsons.toObject(jsonString, Post.class);
    }

    @Test
    public void aa() {
        Post post = new Post();
        post.threadId = 1;
        post.author = "mini";
        post.user = new User("cooper");
        System.out.println(Jsons.toJsonString(post));
    }

    @Test
    public void bb() {
        String json = "{\"threadId\":1,\"author\":\"mini\",\"poster\":{\"name\":\"cooper\"}}";
        Post post = Jsons.toObject(json, Post.class);
        System.out.println(post.user.name);
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

    private static class Post implements ViewModel {
        private int threadId;
        private String author;
        //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        //@JsonSerialize(using = CustomDateSerializer.class)
        private Date committedAt;

        private User user;

        public User getPoster() {
            return this.user;
        }

        public void setPoster(User user) {
            this.user = user;
        }

        private Post() {
        }

        private Post(int threadId, String author, Date aDate) {
            this.threadId = threadId;
            this.author = author;
            this.committedAt = aDate;
        }
    }

    private static class User implements ViewModel {
        private String name;

        public User() {}

        public User(String name) {
            this.name = name;
        }
    }

}