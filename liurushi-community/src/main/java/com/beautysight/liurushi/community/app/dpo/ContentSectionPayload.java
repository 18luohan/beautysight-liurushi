/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.app.dpo;

import com.beautysight.liurushi.common.app.Payload;
import com.beautysight.liurushi.common.ex.IllegalParamException;
import com.beautysight.liurushi.common.utils.Beans;
import com.beautysight.liurushi.common.utils.PreconditionUtils;
import com.beautysight.liurushi.community.domain.work.ContentType;
import com.beautysight.liurushi.community.domain.work.cs.*;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author chenlong
 * @since 1.0
 */
public abstract class ContentSectionPayload extends Payload {

    public String id;
    public ContentType type;

    @JsonCreator
    public static ContentSectionPayload newInstance(@JsonProperty("type") ContentType type) {
        if (type == ContentType.text) {
            return new TextBlockPayload();
        } else if (type == ContentType.image) {
            return new PicturePayload();
        } else if (type == ContentType.video) {
            return new VideoPayload();
        }
        throw new IllegalParamException("Invalid content section type, expected %s, but actual %s",
                ContentType.values(), type);
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
            this.type = ContentType.text;
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

        public RichPayload() {
            super();
        }

        /**
         * Only for view full work 1.0 api.
         * Jackson Json will invoke this method.
         *
         * @return
         */
        public String getPictureUrl() {
            if (this.type == ContentType.image) {
                return this.fileUrl;
            }
            return null;
        }

        @Override
        public Rich toDomainModel() {
            throw new UnsupportedOperationException("Should not invoke toDomainModel method on RichContentSectionPayload");
        }

    }

    public static class PicturePayload extends RichPayload {

        public PicturePayload() {
            this.type = ContentType.image;
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
            this.type = ContentType.video;
        }

        @Override
        public Video toDomainModel() {
            Video target = new Video();
            Beans.copyProperties(this, target);
            return target;
        }
    }

}