/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.app.dpo;

import com.beautysight.liurushi.common.app.DPO;
import com.beautysight.liurushi.common.utils.Beans;
import com.beautysight.liurushi.common.utils.PreconditionUtils;
import com.beautysight.liurushi.community.domain.model.content.*;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author chenlong
 * @since 1.0
 */
public class PresentationDPO extends DPO {

    public List<SlideDPO> slides;
    public Author author;
    public Work.Source source;

    public void validate() {
        PreconditionUtils.checkRequired("presentation.author", author);
        PreconditionUtils.checkRequired("presentation.source", source);
        PreconditionUtils.checkRequired("presentation.slides", slides);
        ControlDPO.validate(slides);
    }

    public Presentation toPresentation() {
        ControlDPO.sortByOrderAsc(slides);
        List<Presentation.Slide> slideList = Lists.newArrayListWithCapacity(slides.size());
        for (SlideDPO dto : slides) {
            slideList.add(dto.toSlide());
        }
        return new Presentation(slideList, author, source);
    }

    public static PresentationDPO from(Presentation source, Map<String, String> keyToDownloadUrlMapping) {
        // translate slide list
        List<SlideDPO> slideDTOs = new ArrayList<>();
        for (Presentation.Slide slide : source.componentParts()) {

            // translate slide
            SlideDPO targetSlideDTO = new SlideDPO();
            Beans.copyProperties(slide, targetSlideDTO);

            // translate content section
            ContentSection section = slide.content();
            if (section instanceof Picture) {
                Picture sourcePicture = (Picture) section;
                ContentSectionDPO.PictureDPO targetPictureDTO = new ContentSectionDPO.PictureDPO();
                Beans.copyProperties(sourcePicture, targetPictureDTO);
                targetPictureDTO.pictureUrl = keyToDownloadUrlMapping.get(sourcePicture.key());

                targetSlideDTO.content = targetPictureDTO;
            } else if (section instanceof TextBlock) {
                TextBlock sourceTextBlock = (TextBlock) section;
                ContentSectionDPO.TextBlockDPO targetTextBlockDTO = new ContentSectionDPO.TextBlockDPO();
                Beans.copyProperties(sourceTextBlock, targetTextBlockDTO);

                targetSlideDTO.content = targetTextBlockDTO;
            }

            slideDTOs.add(targetSlideDTO);
        }

        PresentationDPO target = new PresentationDPO();
        Beans.copyProperties(source, target, "slides");
        target.slides = slideDTOs;
        return target;
    }

    public static class SlideDPO extends ControlDPO {
        public Presentation.Widget textWidget;

        @Override
        public void validate() {
            super.validate();
            // textWidget 可选
            if (textWidget != null) {
                textWidget.validate();
            }
        }

        public Presentation.Slide toSlide() {
            Presentation.Slide target = new Presentation.Slide();
            Beans.copyProperties(this, target);
            return target;
        }
    }
}
