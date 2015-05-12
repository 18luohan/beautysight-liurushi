/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */
package com.beautysight.liurushi.common.utils;

import org.junit.Test;
import org.slf4j.helpers.MessageFormatter;

import static org.junit.Assert.*;

/**
 * Here is Javadoc.
 * <p/>
 * Created by chenlong on 2015-05-12.
 *
 * @author chenlong
 * @since 1.0
 */
public class LogsTest {

    @Test
    public void formatLogMessage() {
        String message = "Authorize request: {} {}, access token: {}";
        String[] args = new String[] { "Post", "/users/1000", "1000-token" };
        String actual = MessageFormatter.arrayFormat(message, args).getMessage();
        String expected = String.format("Authorize request: %s %s, access token: %s", args);
        assertEquals(expected, actual);
    }

}