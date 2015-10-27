/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.app.command;

import com.beautysight.liurushi.common.app.Command;
import com.beautysight.liurushi.common.utils.PreconditionUtils;
import com.beautysight.liurushi.community.app.dpo.ContentSectionPayload;
import com.beautysight.liurushi.community.app.dpo.StoryPayload;
import com.beautysight.liurushi.community.app.dpo.PresentationPayload;
import com.beautysight.liurushi.community.domain.work.cs.ContentSection;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * @author chenlong
 * @since 1.0
 */
public class PublishWorkCommand implements Command {

    public String title;
    public String subtitle;
    public StoryPayload story;
    public PresentationPayload presentation;
    public List<ContentSectionPayload> contentSections;
    public String authorId;

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    /**
     * Only for publish 1.0 api.
     * Jackson Json will invoke this method.
     *
     * @param story
     */
    public void setPictureStory(StoryPayload story) {
        this.story = story;
    }

    public void validate() {
        PreconditionUtils.checkRequired("title", title);
        PreconditionUtils.checkRequired("presentation", presentation);
        PreconditionUtils.checkRequired("story", story);
        presentation.validate();
        story.validate();

        PreconditionUtils.checkRequired("contentSections", contentSections);
        HashSet<String> sectionIds = new HashSet<>();
        for (ContentSectionPayload section : contentSections) {
            section.validate();
            if (sectionIds.contains(section.id)) {
                throw new IllegalArgumentException("Duplicate id in contentSections: " + section.id);
            }
        }

        PreconditionUtils.checkRequired("authorId", authorId);
    }

    public Map<String, ContentSection> contentSectionsMap() {
        Map<String, ContentSection> contentSectionsMap = new HashMap<>();
        for (ContentSectionPayload sectionDTO : contentSections) {
            contentSectionsMap.put(sectionDTO.id, sectionDTO.toDomainModel());
        }
        return contentSectionsMap;
    }

}