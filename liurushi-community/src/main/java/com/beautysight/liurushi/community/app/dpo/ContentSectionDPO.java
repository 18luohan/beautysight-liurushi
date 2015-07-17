/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.app.dpo;

import com.beautysight.liurushi.common.app.DPO;
import com.beautysight.liurushi.common.domain.Dimensions;
import com.beautysight.liurushi.common.ex.IllegalParamException;
import com.beautysight.liurushi.common.utils.Beans;
import com.beautysight.liurushi.common.utils.PreconditionUtils;
import com.beautysight.liurushi.community.domain.model.content.ContentSection;
import com.beautysight.liurushi.community.domain.model.content.Picture;
import com.beautysight.liurushi.community.domain.model.content.TextBlock;
import com.beautysight.liurushi.fundamental.domain.storage.ResourceInStorage;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author chenlong
 * @since 1.0
 */
public abstract class ContentSectionDPO extends DPO {
    public String id;
    public ContentSection.Type type;

    @JsonCreator
    public static ContentSectionDPO newInstance(@JsonProperty("type") ContentSection.Type type) {
        if (type == ContentSection.Type.image) {
            return new PictureDPO();
        } else if (type == ContentSection.Type.text) {
            return new TextBlockDPO();
        }
        throw new IllegalParamException("Invalid content section type, expected %s, but actual %s",
                ContentSection.Type.values(), type);
    }

    public void validate() {
        PreconditionUtils.checkRequired("ContentSection.id", id);
        PreconditionUtils.checkRequired("ContentSection.type", type);
    }

    public abstract ContentSection toDomainModel();

    public static class PictureDPO extends ContentSectionDPO {
        public String name;
        public Picture.Format format;
        public Dimensions dimensions;
        public Integer size;
        public String signature;
        public ResourceInStorage resource;
        public String pictureUrl;

        public PictureDPO() {
            this.type = ContentSection.Type.image;
        }

        public void validate() {
            super.validate();
            PreconditionUtils.checkRequired("Picture.resource", resource);
            resource.validate();

            if (this.dimensions != null) {
                dimensions.validate();
            }
        }

        @Override
        public Picture toDomainModel() {
            Picture target = new Picture();
            Beans.copyProperties(this, target);
            return target;
        }
    }

    public static class TextBlockDPO extends ContentSectionDPO {
        public String text;
        public TextBlock.CharacterStyle charStyle;
        public TextBlock.Alignment alignment;

        public TextBlockDPO() {
            this.type = ContentSection.Type.text;
        }

        public void validate() {
            super.validate();
            PreconditionUtils.checkRequired("TextBlock.text", text);
            PreconditionUtils.checkRequired("TextBlock.charStyle", charStyle);
            PreconditionUtils.checkRequired("TextBlock.alignment", alignment);
        }

        @Override
        public TextBlock toDomainModel() {
            TextBlock target = new TextBlock();
            Beans.copyProperties(this, target);
            return target;
        }
    }

}
