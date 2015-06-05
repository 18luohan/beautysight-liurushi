/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.app;

import com.beautysight.liurushi.common.app.Command;
import com.beautysight.liurushi.common.app.DTO;
import com.beautysight.liurushi.common.domain.Dimensions;
import com.beautysight.liurushi.common.utils.Beans;
import com.beautysight.liurushi.common.utils.PreconditionUtils;
import com.beautysight.liurushi.community.domain.model.content.*;
import com.beautysight.liurushi.identityaccess.domain.model.User;
import com.google.common.collect.Lists;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * Here is Javadoc.
 * <p/>
 * Created by chenlong on 2015-05-22.
 *
 * @author chenlong
 * @since 1.0
 */
public class PublishContentCommand implements Command {

    public List<PictureDTO> pictures;
    public List<TextBlockDTO> textBlocks;
    public PresentationDTO presentation;
    public PictureStoryDTO pictureStory;

    public void setAuthor(User.UserLite author) {
        PreconditionUtils.checkRequired("author", author);
        this.presentation.author = author;
        this.pictureStory.author = author;
    }

    public void validate() {
        PreconditionUtils.checkRequired("presentation", presentation);
        PreconditionUtils.checkRequired("pictureStory", pictureStory);
        presentation.validate();
        pictureStory.validate();
        PreconditionUtils.checkRequired("pictures", pictures);

        HashSet<String> sectionIds = new HashSet<>();
        for (PictureDTO picture : pictures) {
            picture.validate();
            if (sectionIds.contains(picture.id)) {
                throw new IllegalArgumentException("Duplicate id in pictures and textBlocks: " + picture.id);
            }
        }
        if (!CollectionUtils.isEmpty(textBlocks)) {
            for (TextBlockDTO textBlock : textBlocks) {
                textBlock.validate();
                if (sectionIds.contains(textBlock.id)) {
                    throw new IllegalArgumentException("Duplicate id in pictures and textBlocks: " + textBlock.id);
                }
            }
        }
    }

    public Map<String, ContentSection> contentSections() {
        Map<String, ContentSection> contentSections = new HashMap<>();
        if (!CollectionUtils.isEmpty(pictures)) {
            for (PictureDTO pictureDTO : pictures) {
                contentSections.put(pictureDTO.id, pictureDTO.toPicture());
            }
        }
        if (!CollectionUtils.isEmpty(textBlocks)) {
            for (TextBlockDTO textBlockDTO : textBlocks) {
                contentSections.put(textBlockDTO.id, textBlockDTO.toTextBlock());
            }
        }
        return contentSections;
    }

    public static class PictureStoryDTO {
        public PictureStory.Title title;
        public PictureStory.Title subtitle;
        public PictureStory.Layout layout;
        public CoverDTO cover;
        public List<ShotDTO> shots;
        public User.UserLite author;

        public void validate() {
            PreconditionUtils.checkRequired("pictureStory.title", title);
            PreconditionUtils.checkRequired("pictureStory.layout", layout);
            PreconditionUtils.checkRequired("pictureStory.cover", cover);
            PreconditionUtils.checkRequired("pictureStory.shots", shots);
            cover.validate();
            ComponentPartDTO.validate(shots);
        }

        public PictureStory toPictureStory() {
            ComponentPartDTO.sortByOrderAsc(shots);
            List<PictureStory.Shot> shotList = Lists.newArrayListWithCapacity(shots.size());
            for (ShotDTO dto : shots) {
                shotList.add(dto.toShot());
            }
            return new PictureStory(title, subtitle, layout, cover.toCover(), shotList, author);
        }

        public static class ShotDTO extends ComponentPartDTO {

            public PictureStory.LayoutLocation layoutLoc;
            public PictureStory.VisibleArea visibleArea;

            @Override
            public void validate() {
                super.validate();
                PreconditionUtils.checkRequired("shot.layoutLoc", layoutLoc);
                PreconditionUtils.checkRequired("shot.visibleArea", visibleArea);
                layoutLoc.validate();
                visibleArea.validate();
            }

            public PictureStory.Shot toShot() {
                PictureStory.Shot target = new PictureStory.Shot();
                Beans.copyProperties(this, target);
                return target;
            }

        }

        public static class CoverDTO {

            public String sectionId;
            public int whPercentage;
            public PictureStory.VisibleArea visibleArea;

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
    }

    public static class PresentationDTO {
        public List<SlideDTO> slides;
        public User.UserLite author;

        public void validate() {
            PreconditionUtils.checkRequired("presentation.slides", slides);
            ComponentPartDTO.validate(slides);
        }

        public Presentation toPresentation() {
            ComponentPartDTO.sortByOrderAsc(slides);
            List<Presentation.Slide> slideList = Lists.newArrayListWithCapacity(slides.size());
            for (SlideDTO dto : slides) {
                slideList.add(dto.toSlide());
            }
            return new Presentation(slideList, author);
        }

        public static class SlideDTO extends ComponentPartDTO {
            public Presentation.Widget textWidget;

            @Override
            public void validate() {
                super.validate();
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

    public static class ComponentPartDTO extends DTO {

        public String sectionId;
        public int order;

        public void validate() {
            PreconditionUtils.checkRequired(format("%s.sectionId"), sectionId);
            PreconditionUtils.checkGreaterThanZero(format("%s.order"), order);
        }

        public static void validate(List<? extends ComponentPartDTO> parts) {
            Assert.notEmpty(parts, "units must not be null or empty");
            int orderSum = 0;
            for (ComponentPartDTO part : parts) {
                part.validate();
                orderSum += part.order;
            }

            if (orderSum != (1 + parts.size()) * parts.size() / 2) {
                throw new IllegalArgumentException("order must start from 1 and the step must be 1.");
            }
        }

        public static void sortByOrderAsc(List<? extends ComponentPartDTO> parts) {
            Collections.sort(parts, comparatorByOrder);
        }

        private static final Comparator<ComponentPartDTO> comparatorByOrder = new Comparator<ComponentPartDTO>() {
            @Override
            public int compare(ComponentPartDTO o1, ComponentPartDTO o2) {
                return (o1.order - o2.order);
            }
        };

    }

    public static class ContentSectionDTO extends DTO {
        public String id;

        public void validate() {
            PreconditionUtils.checkRequired(format("%s.id"), id);
        }
    }

    public static class PictureDTO extends ContentSectionDTO {
        public String name;
        public Picture.Format format;
        public Dimensions dimensions;
        public int size;
        public String signature;
        public Picture.ResourceInStorage resource;

        public void validate() {
            super.validate();
            PreconditionUtils.checkRequired("Picture.format", format);
            PreconditionUtils.checkRequired("Picture.dimensions", dimensions);
            PreconditionUtils.checkGreaterThanZero("Picture.size", size);
            PreconditionUtils.checkRequired("Picture.resource", resource);
            dimensions.validate();
            resource.validate();
        }

        public Picture toPicture() {
            Picture target = new Picture();
            Beans.copyProperties(this, target);
            return target;
        }
    }

    public static class TextBlockDTO extends ContentSectionDTO {
        public String text;
        public TextBlock.CharacterStyle charStyle;
        public TextBlock.Alignment alignment;

        public void validate() {
            super.validate();
            PreconditionUtils.checkRequired("TextBlock.text", text);
            PreconditionUtils.checkRequired("TextBlock.charStyle", charStyle);
            PreconditionUtils.checkRequired("TextBlock.alignment", alignment);
        }

        public TextBlock toTextBlock() {
            TextBlock target = new TextBlock();
            Beans.copyProperties(this, target);
            return target;
        }
    }

}