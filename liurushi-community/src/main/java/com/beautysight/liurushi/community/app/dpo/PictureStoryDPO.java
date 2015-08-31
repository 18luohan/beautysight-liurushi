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
import com.beautysight.liurushi.community.domain.model.work.layout.Block;
import com.beautysight.liurushi.community.domain.model.work.layout.Layout;
import com.beautysight.liurushi.community.domain.model.work.picstory.Cover;
import com.beautysight.liurushi.community.domain.model.work.picstory.PictureArea;
import com.beautysight.liurushi.community.domain.model.work.picstory.PictureStory;
import com.beautysight.liurushi.community.domain.model.work.picstory.Shot;
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
    public Layout layout;
    public CoverDPO cover;
    public List<ShotDPO> shots;

    public void validate() {
        PreconditionUtils.checkRequired("pictureStory.title", title);
        PreconditionUtils.checkRequired("pictureStory.layout", layout);
        PreconditionUtils.checkRequired("pictureStory.cover", cover);
        PreconditionUtils.checkRequired("pictureStory.shots", shots);
        cover.validate();
        ControlDPO.validate(shots);
    }

    public PictureStory toPictureStory() {
        ControlDPO.sortByOrderAsc(shots);
        List<Shot> shotList = Lists.newArrayListWithCapacity(shots.size());
        for (ShotDPO dto : shots) {
            shotList.add(dto.toShot());
        }
        return new PictureStory(title, subtitle, layout, cover.toCover(), shotList);
    }

    public static PictureStoryDPO from(PictureStory source, Map<String, String> keyToDownloadUrlMapping) {
        // translate cover
        CoverDPO targetCoverDPO = new CoverDPO();
        Beans.copyProperties(source.cover(), targetCoverDPO);
        targetCoverDPO.pictureUrl = keyToDownloadUrlMapping.get(source.cover().pictureKey());

        // translate shot list
        List<ShotDPO> shotDTOs = new ArrayList<>();
        for (Shot shot : source.controls()) {

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
        public PictureArea picArea;

        public String pictureUrl;

        public void validate() {
            PreconditionUtils.checkRequired("cover.sectionId", sectionId);
            if (picArea != null) {
                picArea.validate();
            }
        }

        public Cover toCover() {
            Cover target = new Cover();
            Beans.copyProperties(this, target);
            return target;
        }
    }

    public static class ShotDPO extends ControlDPO {

        // 目前位置属性未要求客户端提供
        public Block.Position position;
        public Block.Size size;
        public PictureArea picAreaInShot;
        public PictureArea picVisibleArea;

        @Override
        public void validate() {
            super.validate();

            if (this.content instanceof ContentSectionDPO.PictureDPO) {
                PreconditionUtils.checkRequired("shot.size", size);
                size.validate();

                if (picAreaInShot != null) {
                    picAreaInShot.validate();
                }

                if (picVisibleArea != null) {
                    picVisibleArea.validate();
                }
            }
        }

        public Shot toShot() {
            Shot target = new Shot();
            Beans.copyProperties(this, target);
            return target;
        }
    }
}
