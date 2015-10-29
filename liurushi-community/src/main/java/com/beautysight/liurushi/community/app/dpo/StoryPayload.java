/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.app.dpo;

import com.beautysight.liurushi.common.app.Payload;
import com.beautysight.liurushi.common.utils.Beans;
import com.beautysight.liurushi.common.utils.PreconditionUtils;
import com.beautysight.liurushi.community.domain.work.cs.ContentSection;
import com.beautysight.liurushi.community.domain.work.ContentType;
import com.beautysight.liurushi.community.domain.work.cs.Rich;
import com.beautysight.liurushi.community.domain.work.cs.TextBlock;
import com.beautysight.liurushi.community.domain.work.layout.Block;
import com.beautysight.liurushi.community.domain.work.layout.Layout;
import com.beautysight.liurushi.community.domain.work.picstory.Cover;
import com.beautysight.liurushi.community.domain.work.picstory.PictureArea;
import com.beautysight.liurushi.community.domain.work.picstory.Shot;
import com.beautysight.liurushi.community.domain.work.picstory.Story;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author chenlong
 * @since 1.0
 */
public class StoryPayload extends Payload {

    public Layout layout;
    public CoverDPO cover;
    public List<ShotPayload> shots;

    public void validate() {
        PreconditionUtils.checkRequired("story.layout", layout);
        PreconditionUtils.checkRequired("story.cover", cover);
        PreconditionUtils.checkRequired("story.shots", shots);
        cover.validate();
        ControlPayload.validate(shots);
    }

    public Story toPictureStory() {
        ControlPayload.sortByOrderAsc(shots);
        List<Shot> shotList = Lists.newArrayListWithCapacity(shots.size());
        for (ShotPayload dto : shots) {
            shotList.add(dto.toShot());
        }
        return new Story(layout, cover.toCover(), shotList);
    }

    public static StoryPayload from(Story source, Map<String, String> keyToDownloadUrlMapping) {
        // translate cover
        CoverDPO targetCoverDPO = new CoverDPO();
        Beans.copyProperties(source.cover(), targetCoverDPO);
        targetCoverDPO.content = transformRichContentSection(
                source.cover().getContent(), keyToDownloadUrlMapping);

        // translate shot list
        List<ShotPayload> shotDTOs = new ArrayList<>();
        for (Shot shot : source.controls()) {

            // translate shot
            ShotPayload targetShotDTO = new ShotPayload();
            Beans.copyProperties(shot, targetShotDTO);

            // translate content section
            ContentSection section = shot.content();
            if (section instanceof Rich) {
                ContentSectionPayload.RichPayload targetDTO = transformRichContentSection(
                        (Rich) section, keyToDownloadUrlMapping);

                targetShotDTO.content = targetDTO;

            } else if (section instanceof TextBlock) {
                TextBlock sourceTextBlock = (TextBlock) section;
                ContentSectionPayload.TextBlockPayload targetTextBlockDTO = new ContentSectionPayload.TextBlockPayload();
                Beans.copyProperties(sourceTextBlock, targetTextBlockDTO);

                targetShotDTO.content = targetTextBlockDTO;
            }

            shotDTOs.add(targetShotDTO);
        }

        StoryPayload target = new StoryPayload();
        Beans.copyProperties(source, target, "cover", "shots");
        target.cover = targetCoverDPO;
        target.shots = shotDTOs;
        return target;
    }

    private static ContentSectionPayload.RichPayload transformRichContentSection(Rich richContent, Map<String, String> keyToDownloadUrlMapping) {
        ContentSectionPayload.RichPayload targetDTO = new ContentSectionPayload.RichPayload();
        Beans.copyProperties(richContent, targetDTO);
        targetDTO.fileUrl = keyToDownloadUrlMapping.get(richContent.fileKey());
        return targetDTO;
    }

    public static class CoverDPO {
        public String sectionId;
        public PictureArea picArea;

        public ContentSectionPayload.RichPayload content;

        /**
         * Only for view full work 1.0 api.
         * Jackson Json will invoke this method.
         *
         * @return
         */
        public String getPictureUrl() {
            if (content.type == ContentType.image) {
                return this.content.fileUrl;
            }
            return null;
        }

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
