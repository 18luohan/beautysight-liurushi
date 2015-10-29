/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.domain.work.cs;

import com.beautysight.liurushi.common.domain.AbstractEntity;
import com.beautysight.liurushi.community.domain.work.ContentType;
import com.mongodb.DBObject;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.mapping.MappingException;

//import org.mongodb.morphia.annotations.StaticFactoryMethod;

/**
 * @author chenlong
 * @since 1.0
 */
//@StaticFactoryMethod("newInstanceByType")
@Entity(value = "content_sections", noClassnameStored = true)
public abstract class ContentSection extends AbstractEntity {

    protected ContentType type;

    public ContentType type() {
        return this.type;
    }

    public static ContentSection newInstanceByType(DBObject dbObject) {
        String typeValue = (String) dbObject.get("type");

        try {
            ContentType actualType = ContentType.valueOf(typeValue);
            if (actualType == ContentType.text) {
                return new TextBlock();
            } else if (actualType == ContentType.image) {
                return new Picture();
            } else if (actualType == ContentType.video) {
                return new Video();
            } else {
                throw new MappingException("Can't mapping dbObject to specific entity by type filed:" + typeValue);
            }
        } catch (MappingException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new MappingException("Can't mapping dbObject to specific entity by type filed:" + typeValue, ex);
        }
    }

    public static Class[] subclasses() {
        return new Class[]{
                Rich.class,
                Picture.class,
                Video.class,
                TextBlock.class
        };
    }

}