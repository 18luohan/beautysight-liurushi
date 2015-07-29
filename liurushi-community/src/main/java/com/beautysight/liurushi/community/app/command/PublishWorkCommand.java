/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.app.command;

import com.beautysight.liurushi.common.app.Command;
import com.beautysight.liurushi.common.ex.IllegalDomainStateException;
import com.beautysight.liurushi.common.utils.PreconditionUtils;
import com.beautysight.liurushi.community.app.dpo.ContentSectionDPO;
import com.beautysight.liurushi.community.app.dpo.PictureStoryDPO;
import com.beautysight.liurushi.community.app.dpo.PresentationDPO;
import com.beautysight.liurushi.community.domain.model.content.Author;
import com.beautysight.liurushi.community.domain.model.content.ContentSection;
import com.beautysight.liurushi.community.domain.model.content.Work;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * @author chenlong
 * @since 1.0
 */
public class PublishWorkCommand implements Command {

    public List<ContentSectionDPO> contentSections;
    public PresentationDPO presentation;
    public PictureStoryDPO pictureStory;

    public void setAuthor(Author author) {
        PreconditionUtils.checkRequired("author", author);
        this.presentation.author = author;
        this.pictureStory.author = author;
        this.setWorkSourceBy(author);
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
    }

    public Map<String, ContentSection> contentSections() {
        Map<String, ContentSection> contentSectionsMap = new HashMap<>();
        for (ContentSectionDPO sectionDTO : contentSections) {
            contentSectionsMap.put(sectionDTO.id, sectionDTO.toDomainModel());
        }
        return contentSectionsMap;
    }

    private void setWorkSourceBy(Author author) {
        Work.Source source;
        if (author.group == Author.Group.professional) {
            source = Work.Source.pgc;
        } else if (author.group == Author.Group.amateur) {
            source = Work.Source.ugc;
        } else {
            throw new IllegalDomainStateException("Author.group:" + author.group);
        }
        this.presentation.source = source;
        this.pictureStory.source = source;
    }

}