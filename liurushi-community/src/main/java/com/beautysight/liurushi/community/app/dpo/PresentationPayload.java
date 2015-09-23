/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.app.dpo;

import com.beautysight.liurushi.common.app.Payload;
import com.beautysight.liurushi.common.utils.Beans;
import com.beautysight.liurushi.common.utils.PreconditionUtils;
import com.beautysight.liurushi.community.domain.work.cs.ContentSection;
import com.beautysight.liurushi.community.domain.work.cs.Picture;
import com.beautysight.liurushi.community.domain.work.cs.TextBlock;
import com.beautysight.liurushi.community.domain.work.present.Presentation;
import com.beautysight.liurushi.community.domain.work.present.Slide;
import com.beautysight.liurushi.community.domain.work.present.Widget;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author chenlong
 * @since 1.0
 */
public class PresentationPayload extends Payload {

    public List<SlidePayload> slides;

    public void validate() {
        PreconditionUtils.checkRequired("presentation.slides", slides);
        ControlPayload.validate(slides);
    }

    public Presentation toPresentation() {
        ControlPayload.sortByOrderAsc(slides);
        List<Slide> slideList = Lists.newArrayListWithCapacity(slides.size());
        for (SlidePayload dto : slides) {
            slideList.add(dto.toSlide());
        }
        return new Presentation(slideList);
    }

    public static PresentationPayload from(Presentation source, Map<String, String> keyToDownloadUrlMapping) {
        // translate slide list
        List<SlidePayload> slideDTOs = new ArrayList<>();
        for (Slide slide : source.controls()) {

            // translate slide
            SlidePayload targetSlideDTO = new SlidePayload();
            Beans.copyProperties(slide, targetSlideDTO);

            // translate content section
            ContentSection section = slide.content();
            if (section instanceof Picture) {
                Picture sourcePicture = (Picture) section;
                ContentSectionPayload.PicturePayload targetPictureDTO = new ContentSectionPayload.PicturePayload();
                Beans.copyProperties(sourcePicture, targetPictureDTO);
                targetPictureDTO.pictureUrl = keyToDownloadUrlMapping.get(sourcePicture.key());

                targetSlideDTO.content = targetPictureDTO;
            } else if (section instanceof TextBlock) {
                TextBlock sourceTextBlock = (TextBlock) section;
                ContentSectionPayload.TextBlockPayload targetTextBlockDTO = new ContentSectionPayload.TextBlockPayload();
                Beans.copyProperties(sourceTextBlock, targetTextBlockDTO);

                targetSlideDTO.content = targetTextBlockDTO;
            }

            slideDTOs.add(targetSlideDTO);
        }

        PresentationPayload target = new PresentationPayload();
        Beans.copyProperties(source, target, "slides");
        target.slides = slideDTOs;
        return target;
    }

    public static class SlidePayload extends ControlPayload {
        public Widget textWidget;

        @Override
        public void validate() {
            super.validate();
            // textWidget 可选
            if (textWidget != null) {
                textWidget.validate();
            }
        }

        public Slide toSlide() {
            Slide target = new Slide();
            Beans.copyProperties(this, target);
            return target;
        }
    }
}
