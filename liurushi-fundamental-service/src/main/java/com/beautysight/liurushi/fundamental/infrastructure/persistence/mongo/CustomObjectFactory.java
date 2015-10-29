/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.fundamental.infrastructure.persistence.mongo;

import com.mongodb.DBObject;
import org.mongodb.morphia.mapping.DefaultCreator;
import org.mongodb.morphia.mapping.MappingException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chenlong
 * @since 1.0
 */
public class CustomObjectFactory extends DefaultCreator {

    private static final String CLASSNAME_METHODNAME_DELIMITER = ".";
    private static final String SUBCLASSES_METHODNAME = "subclasses";

    private Map<Class<?>, Method> staticFactoryMethodsForPOJO;

    /**
     * This method is defined for Spring.
     *
     * @param staticFactoryMethods
     */
    public void setStaticFactoryMethodsForPOJO(List<String> staticFactoryMethods) {
        if (CollectionUtils.isEmpty(staticFactoryMethods)) {
            throw new IllegalArgumentException(
                    "Static factory method string list is empty. "
                            + "A static factory method string for POJO need to be in format 'a.full.qualified.ClassName.methodName'!");
        }

        Map<Class<?>, Method> mappings = new HashMap<>();
        for (String methodString : staticFactoryMethods) {
            methodString = methodString.trim();
            int classNameMethodNameSeparationIndex = methodString.lastIndexOf(CLASSNAME_METHODNAME_DELIMITER);
            if (classNameMethodNameSeparationIndex >= methodString.length() - 1) {
                throw new IllegalArgumentException(
                        "Static factory method string for POJO need to be in format 'a.full.qualified.ClassName.methodName'!");
            }

            String fullQualifiedClassName = methodString.substring(0, classNameMethodNameSeparationIndex);
            String staticMethodName = methodString.substring(classNameMethodNameSeparationIndex + 1);

            Class<?> clazz;
            try {
                clazz = Class.forName(fullQualifiedClassName, true, getClassLoaderForClass());
            } catch (ClassNotFoundException e) {
                throw new BeanInitializationException(
                        "Can't find class: " + fullQualifiedClassName, e);
            }

            Method staticMethod;
            try {
                staticMethod = clazz.getDeclaredMethod(staticMethodName, DBObject.class);
            } catch (NoSuchMethodException e) {
                throw new BeanInitializationException(
                        "Can't find method " + staticMethodName, e);
            }

            // Record static factory method for a super class
            mappings.put(clazz, staticMethod);

            Method subclassesMethod;
            try {
                subclassesMethod = clazz.getDeclaredMethod(SUBCLASSES_METHODNAME);
                Class[] subclasses = (Class[]) subclassesMethod.invoke(null);
                if (subclasses != null && subclasses.length > 0) {

                    // Record static factory method for every sub class
                    for (Class subclass : subclasses) {
                        mappings.put(subclass, staticMethod);
                    }
                }
            } catch (NoSuchMethodException e) {
                throw new BeanInitializationException(
                        "Can't find method " + SUBCLASSES_METHODNAME, e);
            } catch (IllegalAccessException e) {
                throw new BeanInitializationException(
                        String.format("Can't invoke method %s, please check its modifiers", SUBCLASSES_METHODNAME),
                        e);
            } catch (InvocationTargetException e) {
                throw new BeanInitializationException(
                        String.format("Can't invoke method %s on the given object, please check invocation target", SUBCLASSES_METHODNAME),
                        e);
            }
        }

        this.staticFactoryMethodsForPOJO = Collections.unmodifiableMap(mappings);
    }

    @Override
    public <T> T createInstance(final Class<T> clazz, final DBObject dbObj) {
        T inst = createInstByStaticFactoryMethod(clazz, dbObj);
        if (inst != null) {
            return inst;
        }
        return super.createInstance(clazz, dbObj);
    }

    private <T> T createInstByStaticFactoryMethod(final Class<T> clazz, final DBObject dbObject) {
        if (clazz == null) {
            return null;
        }

        try {
            Method method = staticFactoryMethodsForPOJO.get(clazz);
            if (method == null) {
                return null;
            }
            return (T) method.invoke(null, dbObject);
        } catch (Exception e) {
            throw new MappingException("No usable static factory method for " + clazz.getName(), e);
        }
    }

}
