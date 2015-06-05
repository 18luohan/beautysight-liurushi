/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.domain.model.content;

import com.beautysight.liurushi.common.domain.ValueObject;
import org.mongodb.morphia.annotations.Reference;

/**
 * A key abstraction that is a component part of a {@link PictureStory} or {@link Presentation}
 *
 * @author chenlong
 * @since 1.0
 */
public abstract class ComponentPart extends ValueObject {

    @Reference(value = "sectionId", idOnly = true)
    private ContentSection content;

    /**
     * the order of this composite unit in the whole.
     */
    private int order;

    public void setContentSection(ContentSection content) {
        this.content = content;
    }

}
