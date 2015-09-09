/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.app.dpo;

import com.beautysight.liurushi.common.app.Payload;
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
public class PictureStoryPayload extends Payload {

    public Layout layout;
    public CoverDPO cover;
    public List<ShotPayload> shots;

    public void validate() {
        PreconditionUtils.checkRequired("pictureStory.layout", layout);
        PreconditionUtils.checkRequired("pictureStory.cover", cover);
        PreconditionUtils.checkRequired("pictureStory.shots", shots);
        cover.validate();
        ControlPayload.validate(shots);
    }

    public PictureStory toPictureStory() {
        ControlPayload.sortByOrderAsc(shots);
        List<Shot> shotList = Lists.newArrayListWithCapacity(shots.size());
        for (ShotPayload dto : shots) {
            shotList.add(dto.toShot());
        }
        return new PictureStory(layout, cover.toCover(), shotList);
    }

    public static PictureStoryPayload from(PictureStory source, Map<String, String> keyToDownloadUrlMapping) {
        // translate cover
        CoverDPO targetCoverDPO = new CoverDPO();
        Beans.copyProperties(source.cover(), targetCoverDPO);
        targetCoverDPO.pictureUrl = keyToDownloadUrlMapping.get(source.cover().pictureKey());

        // translate shot list
        List<ShotPayload> shotDTOs = new ArrayList<>();
        for (Shot shot : source.controls()) {

            // translate shot
            ShotPayload targetShotDTO = new ShotPayload();
            Beans.copyProperties(shot, targetShotDTO);

            // translate content section
            ContentSection section = shot.content();
            if (section instanceof Picture) {
                Picture sourcePicture = (Picture) section;
                ContentSectionPayload.PicturePayload targetPictureDTO = new ContentSectionPayload.PicturePayload();
                Beans.copyProperties(sourcePicture, targetPictureDTO);
                targetPictureDTO.pictureUrl = keyToDownloadUrlMapping.get(sourcePicture.key());

                targetShotDTO.content = targetPictureDTO;
            } else if (section instanceof TextBlock) {
                TextBlock sourceTextBlock = (TextBlock) section;
                ContentSectionPayload.TextBlockPayload targetTextBlockDTO = new ContentSectionPayload.TextBlockPayload();
                Beans.copyProperties(sourceTextBlock, targetTextBlockDTO);

                targetShotDTO.content = targetTextBlockDTO;
            }

            shotDTOs.add(targetShotDTO);
        }

        PictureStoryPayload target = new PictureStoryPayload();
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

    public static class ShotPayload extends ControlPayload {

        // 目前位置属性未要求客户端提供
        public Block.Position position;
        public Block.Size size;
        public PictureArea picAreaInShot;
        public PictureArea picVisibleArea;

        @Override
        public void validate() {
            super.validate();

            if (this.content instanceof ContentSectionPayload.PicturePayload) {
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