/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.app.command;

import com.beautysight.liurushi.common.app.Command;
import com.beautysight.liurushi.common.utils.PreconditionUtils;
import com.beautysight.liurushi.community.app.dpo.ContentSectionDPO;
import com.beautysight.liurushi.community.app.dpo.PictureStoryDPO;
import com.beautysight.liurushi.community.app.dpo.PresentationDPO;
import com.beautysight.liurushi.community.domain.model.work.Author;
import com.beautysight.liurushi.community.domain.model.work.cs.ContentSection;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * @author chenlong
 * @since 1.0
 */
public class PublishWorkCommand implements Command {

    public PictureStoryDPO pictureStory;
    public PresentationDPO presentation;
    public List<ContentSectionDPO> contentSections;
    public Author author;

    public void setAuthor(Author author) {
        this.author = author;
    }

    public void validate() {
        PreconditionUtils.checkRequired("presentation", presentation);
        PreconditionUtils.checkRequired("pictureStory", pictureStory);
        presentation.validate();
        pictureStory.validate();

        PreconditionUtils.checkRequired("contentSections", contentSections);
        HashSet<String> sectionIds = new HashSet<>();
        for (ContentSectionDPO section : contentSections) {
            section.validate();
            if (sectionIds.contains(section.id)) {
                throw new IllegalArgumentException("Duplicate id in contentSections: " + section.id);
            }
        }

        PreconditionUtils.checkRequired("author", author);
    }

    public Map<String, ContentSection> contentSectionsMap() {
        Map<String, ContentSection> contentSectionsMap = new HashMap<>();
        for (ContentSectionDPO sectionDTO : contentSections) {
            contentSectionsMap.put(sectionDTO.id, sectionDTO.toDomainModel());
        }
        return contentSectionsMap;
    }

}