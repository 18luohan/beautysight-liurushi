/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.common.utils;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author chenlong
 * @since 1.0
 */
public class RegexpTest {

    @Test
    public void isEmail() {
        assertFalse(Regexp.isEmail(""));
        assertFalse(Regexp.isEmail("lily"));
        assertFalse(Regexp.isEmail("lily@g"));
        assertFalse(Regexp.isEmail("lily-@g-k.com"));
        assertFalse(Regexp.isEmail("lily.@g-k.com"));

        assertTrue(Regexp.isEmail("lily@g.c"));
        assertTrue(Regexp.isEmail("lily@g-k.com"));
        assertTrue(Regexp.isEmail("lily_@g-k.com"));
        assertTrue(Regexp.isEmail("lily_lee@g-k.com"));
        assertTrue(Regexp.isEmail("lily-lee@g-k.com"));
        assertTrue(Regexp.isEmail("lily.lee@g-k.com"));
    }

    @Test
    public void isMobile() {
        assertFalse(Regexp.isMobile("1"));
        assertFalse(Regexp.isMobile("12345678901"));
        assertFalse(Regexp.isMobile("15824464622"));
        assertFalse(Regexp.isMobile("+15824464622"));
        assertFalse(Regexp.isMobile("-15824464622"));
        assertFalse(Regexp.isMobile("+-15824464622"));
        assertFalse(Regexp.isMobile("-345-15824464622"));
        assertFalse(Regexp.isMobile("-345--15824464622"));
        assertFalse(Regexp.isMobile("+-345-15824464622"));

        assertTrue(Regexp.isMobile("+1-15824464622"));
        assertTrue(Regexp.isMobile("+1-1-15824464622"));
        // 开曼群岛国际区号：+1-345
        assertTrue(Regexp.isMobile("+1-345-15824464622"));
        assertTrue(Regexp.isMobile("+86-15824464622"));
        assertTrue(Regexp.isMobile("+389-1-15824464622"));
    }

}