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
 * @author chenlong
 * @since 1.0
 */
@StaticFactoryMethod("newInstanceByType")
@Entity(value = "content_sections", noClassnameStored = true)
public abstract class ContentSection extends AbstractEntity {

    protected Type type;

    public Type type() {
        return this.type;
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

    // TODO 待删除
//    public void validate() {
//        PreconditionUtils.checkRequired(format("%s.type"), type);
//    }

    public enum Type {
        image, text
    }

}
