/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.app.dpo;

import com.beautysight.liurushi.common.app.Payload;
import com.beautysight.liurushi.common.ex.IllegalParamException;
import com.beautysight.liurushi.common.utils.Beans;
import com.beautysight.liurushi.common.utils.PreconditionUtils;
import com.beautysight.liurushi.community.domain.work.cs.*;
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
        if (type == ContentSection.Type.text) {
            return new TextBlockPayload();
        } else if (type == ContentSection.Type.image) {
            return new PicturePayload();
        } else if (type == ContentSection.Type.video) {
            return new VideoPayload();
        }
        throw new IllegalParamException("Invalid content section type, expected %s, but actual %s",
                ContentSection.Type.values(), type);
    }

    public void validate() {
        PreconditionUtils.checkRequired("ContentSection.id", id);
        PreconditionUtils.checkRequired("ContentSection.type", type);
    }

    public abstract ContentSection toDomainModel();

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

    public static class RichPayload extends ContentSectionPayload {

        public String fileUrl;

        // for api 1.0 presentation
        public String pictureUrl;

        public RichPayload() {
            super();
        }

        @Override
        public Rich toDomainModel() {
            throw new UnsupportedOperationException("Should not invoke toDomainModel method on RichContentSectionPayload");
        }

    }

    public static class PicturePayload extends RichPayload {

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

    public static class VideoPayload extends RichPayload {

        public VideoPayload() {
            this.type = ContentSection.Type.video;
        }

        @Override
        public Video toDomainModel() {
            Video target = new Video();
            Beans.copyProperties(this, target);
            return target;
        }
    }

}