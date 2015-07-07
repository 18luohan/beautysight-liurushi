/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.test.utils;

import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author chenlong
 * @since 1.0
 */
public class Reflections {

    private static final String temp = "temp";

    public static <T> T newInstanceUsingDefaultConstructor(Class<T> clazz) {
        try {
            Constructor<T> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (Exception ex) {
            throw new RuntimeException(
                    String.format("Error while creating instance of %s using default constructor", clazz.getSimpleName()),
                    ex);
        }
    }

    public static void setField(Object target, String name, Object value) {
        Field field = ReflectionUtils.findField(target.getClass(), name);
        ReflectionUtils.makeAccessible(field);
        ReflectionUtils.setField(field, target, value);
    }

    public static <T> T getField(Object target, String name) {
        Field field = ReflectionUtils.findField(target.getClass(), name);
        ReflectionUtils.makeAccessible(field);
        return (T) ReflectionUtils.getField(field, target);
    }

    public static void main(String[] args) {
        Method[] methods = ReflectionUtils.getAllDeclaredMethods(Reflections.class);
        for (Method m : methods) {
            System.out.println(m.getName());
            System.out.println(m.toGenericString());
            System.out.println(m.toString());
        }

        Field field = ReflectionUtils.findField(Reflections.class, "temp");
        System.out.println(field);
        System.out.println(field.getName());
        System.out.println(field.toGenericString());
        System.out.println(field.toString());
    }

}
