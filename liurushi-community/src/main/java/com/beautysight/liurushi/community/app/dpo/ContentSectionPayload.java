/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.app.dpo;

import com.beautysight.liurushi.common.app.Payload;
import com.beautysight.liurushi.common.ex.IllegalParamException;
import com.beautysight.liurushi.common.utils.Beans;
import com.beautysight.liurushi.common.utils.PreconditionUtils;
import com.beautysight.liurushi.community.domain.work.cs.ContentSection;
import com.beautysight.liurushi.community.domain.work.cs.Picture;
import com.beautysight.liurushi.community.domain.work.cs.TextBlock;
import com.beautysight.liurushi.fundamental.domain.storage.FileMetadata;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author chenlong
 * @since 1.0
 */
public abstract class ContentSectionPayload extends Payload {

    public String id;
    public ContentSection.Type type;

    @JsonCreator
    public static ContentSectionPayload newInstance(@JsonProperty("type") ContentSection.Type type) {
        if (type == ContentSection.Type.image) {
            return new PicturePayload();
        } else if (type == ContentSection.Type.text) {
            return new TextBlockPayload();
        }
        throw new IllegalParamException("Invalid content section type, expected %s, but actual %s",
                ContentSection.Type.values(), type);
    }

    public void validate() {
        PreconditionUtils.checkRequired("ContentSection.id", id);
        PreconditionUtils.checkRequired("ContentSection.type", type);
    }

    public abstract ContentSection toDomainModel();

    public static class PicturePayload extends ContentSectionPayload {
        public Picture.Format format;

        //下面的字段在展现时使用
        private FileMetadata resource;
        public String pictureUrl;

        public PicturePayload() {
            this.type = ContentSection.Type.image;
        }

        @Override
        public Picture toDomainModel() {
            Picture target = new Picture();
            Beans.copyProperties(this, target);
            return target;
        }
    }

    public static class TextBlockPayload extends ContentSectionPayload {
        public String text;
        public TextBlock.CharacterStyle charStyle;
        public TextBlock.Alignment alignment;

        public TextBlockPayload() {
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
