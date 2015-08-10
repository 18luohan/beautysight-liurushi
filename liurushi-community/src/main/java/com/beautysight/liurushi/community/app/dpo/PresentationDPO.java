/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.app.dpo;

import com.beautysight.liurushi.common.app.DPO;
import com.beautysight.liurushi.common.utils.Beans;
import com.beautysight.liurushi.common.utils.PreconditionUtils;
import com.beautysight.liurushi.community.domain.model.work.cs.ContentSection;
import com.beautysight.liurushi.community.domain.model.work.cs.Picture;
import com.beautysight.liurushi.community.domain.model.work.cs.TextBlock;
import com.beautysight.liurushi.community.domain.model.work.present.Presentation;
import com.beautysight.liurushi.community.domain.model.work.present.Slide;
import com.beautysight.liurushi.community.domain.model.work.present.Widget;
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

    public void validate() {
        PreconditionUtils.checkRequired("presentation.slides", slides);
        ControlDPO.validate(slides);
    }

    public Presentation toPresentation() {
        ControlDPO.sortByOrderAsc(slides);
        List<Slide> slideList = Lists.newArrayListWithCapacity(slides.size());
        for (SlideDPO dto : slides) {
            slideList.add(dto.toSlide());
        }
        return new Presentation(slideList);
    }

    public static PresentationDPO from(Presentation source, Map<String, String> keyToDownloadUrlMapping) {
        // translate slide list
        List<SlideDPO> slideDTOs = new ArrayList<>();
        for (Slide slide : source.controls()) {

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
