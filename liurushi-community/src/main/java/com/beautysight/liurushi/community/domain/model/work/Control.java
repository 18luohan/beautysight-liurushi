/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.domain.model.work;

import com.beautysight.liurushi.common.domain.ValueObject;
import com.beautysight.liurushi.community.domain.model.work.cs.ContentSection;
import com.beautysight.liurushi.community.domain.model.work.picstory.PictureStory;
import com.beautysight.liurushi.community.domain.model.work.present.Presentation;
import org.mongodb.morphia.annotations.Reference;

/**
 * A key abstraction that is a control of a {@link PictureStory} or {@link Presentation}
 *
 * @author chenlong
 * @since 1.0
 */
public abstract class Control extends ValueObject {

    @Reference(value = "sectionId", idOnly = true)
    private ContentSection content;

    /**
     * the order of this composite unit in the whole.
     */
    private Integer order;

    public void setContentSection(ContentSection content) {
        this.content = content;
    }

    public ContentSection content() {
        return content;
    }

    public Integer order() {
        return this.order;
    }

}
