/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.domain.model.work;

import com.beautysight.liurushi.common.domain.ValueObject;
import com.beautysight.liurushi.community.domain.model.work.cs.ContentSection;
import com.beautysight.liurushi.community.domain.model.work.picstory.PictureStory;
import com.beautysight.liurushi.community.domain.model.work.present.Presentation;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Transient;

/**
 * A key abstraction that is a control of a {@link PictureStory} or {@link Presentation}
 *
 * @author chenlong
 * @since 1.0
 */
public abstract class Control extends ValueObject {

    private ObjectId sectionId;

    @Transient
    private ContentSection content;

    /**
     * the order of this composite unit in the whole.
     */
    private Integer order;

    public void setContentSection(ContentSection content) {
        this.sectionId = content.id();
        this.content = content;
    }

    public ContentSection content() {
        return this.content;
    }

    public ObjectId sectionId() {
        return this.sectionId;
    }

    public Integer order() {
        return this.order;
    }

}