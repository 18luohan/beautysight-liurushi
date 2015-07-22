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
public class PictureStoryDPO extends DPO {
    
    public String title;
    public String subtitle;
    public PictureStory.Layout layout;
    public CoverDPO cover;
    public List<ShotDPO> shots;
    public Author author;
    public Work.Source source;

    public void validate() {
        PreconditionUtils.checkRequired("pictureStory.title", title);
        PreconditionUtils.checkRequired("pictureStory.layout", layout);
        PreconditionUtils.checkRequired("pictureStory.cover", cover);
        PreconditionUtils.checkRequired("pictureStory.shots", shots);
        cover.validate();
        ControlDPO.validate(shots);

        PreconditionUtils.checkRequired("pictureStory.author", author);
        PreconditionUtils.checkRequired("pictureStory.source", source);
    }

    public PictureStory toPictureStory() {
        ControlDPO.sortByOrderAsc(shots);
        List<PictureStory.Shot> shotList = Lists.newArrayListWithCapacity(shots.size());
        for (ShotDPO dto : shots) {
            shotList.add(dto.toShot());
        }
        return new PictureStory(title, subtitle, layout, cover.toCover(), shotList, author, source);
    }

    public static PictureStoryDPO from(PictureStory source, Map<String, String> keyToDownloadUrlMapping) {
        // translate cover
        CoverDPO targetCoverDPO = new CoverDPO();
        Beans.copyProperties(source.cover(), targetCoverDPO);
        targetCoverDPO.pictureUrl = keyToDownloadUrlMapping.get(source.cover().pictureKey());

        // translate shot list
        List<ShotDPO> shotDTOs = new ArrayList<>();
        for (PictureStory.Shot shot : source.componentParts()) {

            // translate shot
            ShotDPO targetShotDTO = new ShotDPO();
            Beans.copyProperties(shot, targetShotDTO);

            // translate content section
            ContentSection section = shot.content();
            if (section instanceof Picture) {
                Picture sourcePicture = (Picture) section;
                ContentSectionDPO.PictureDPO targetPictureDTO = new ContentSectionDPO.PictureDPO();
                Beans.copyProperties(sourcePicture, targetPictureDTO);
                targetPictureDTO.pictureUrl = keyToDownloadUrlMapping.get(sourcePicture.key());

                targetShotDTO.content = targetPictureDTO;
            } else if (section instanceof TextBlock) {
                TextBlock sourceTextBlock = (TextBlock) section;
                ContentSectionDPO.TextBlockDPO targetTextBlockDTO = new ContentSectionDPO.TextBlockDPO();
                Beans.copyProperties(sourceTextBlock, targetTextBlockDTO);

                targetShotDTO.content = targetTextBlockDTO;
            }

            shotDTOs.add(targetShotDTO);
        }

        PictureStoryDPO target = new PictureStoryDPO();
        Beans.copyProperties(source, target, "cover", "shots");
        target.cover = targetCoverDPO;
        target.shots = shotDTOs;
        return target;
    }

    public static class CoverDPO {
        public String sectionId;
        public int whPercentage;
        public PictureStory.VisibleArea visibleArea;

        public String pictureUrl;

        public void validate() {
            PreconditionUtils.checkRequired("cover.sectionId", sectionId);
            PreconditionUtils.checkGreaterThanZero("cover.whPercentage", whPercentage);
            if (visibleArea != null) {
                visibleArea.validate();
            }
        }

        public PictureStory.Cover toCover() {
            PictureStory.Cover target = new PictureStory.Cover();
            Beans.copyProperties(this, target);
            return target;
        }
    }

    public static class ShotDPO extends ControlDPO {

        public PictureStory.ControlSize size;
        public PictureStory.VisibleArea visibleArea;

        @Override
        public void validate() {
            super.validate();
            PreconditionUtils.checkRequired("shot.size", size);
            size.validate();

            // TODO visibleArea 目前可选
            //PreconditionUtils.checkRequired("shot.visibleArea", visibleArea);
            if (visibleArea != null) {
                visibleArea.validate();
            }
        }

        public PictureStory.Shot toShot() {
            PictureStory.Shot target = new PictureStory.Shot();
            Beans.copyProperties(this, target);
            return target;
        }

    }
}
