/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.domain.model.content;

import com.beautysight.liurushi.common.domain.AbstractEntity;
import com.beautysight.liurushi.common.utils.PreconditionUtils;
import com.mongodb.DBObject;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.StaticFactoryMethod;
import org.mongodb.morphia.mapping.MappingException;

import java.lang.reflect.Method;

/**
 * A key abstract that represents story section.
 *
 * @author chenlong
 * @since 1.0
 */
@StaticFactoryMethod("newInstanceByType")
@Entity(value = "content_sections", noClassnameStored = true)
public abstract class ContentSection extends AbstractEntity {

    protected Type type;

    public void validate() {
        PreconditionUtils.checkRequired(format("%s.type"), type);
    }

    public enum Type {
        image, text
    }

    public static ContentSection newInstanceByType(DBObject dbObject) {
        String typeValue = (String) dbObject.get("type");

        try {
            Type actualType = Type.valueOf(typeValue);
            if (actualType == Type.image) {
                return new Picture();
            } else if (actualType == Type.text) {
                return new TextBlock();
            } else {
                throw new MappingException("Can't mapping dbObject to specific entity by type filed:" + typeValue);
            }
        } catch (MappingException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new MappingException("Can't mapping dbObject to specific entity by type filed:" + typeValue, ex);
        }
    }

    public static void say(String words) {
        System.out.println("hello " + words);
    }

    public static void main(String[] args) {
        try {
            Method method = ContentSection.class.getDeclaredMethod("say", String.class);
            method.invoke(null, "guava");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
