/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.app;

import com.beautysight.liurushi.community.app.command.PublishWorkCommand;
import com.beautysight.liurushi.community.app.dpo.ControlDPO;
import com.beautysight.liurushi.community.app.presentation.WorkPresentation;
import com.beautysight.liurushi.community.app.presentation.WorkProfilePresentation;
import com.beautysight.liurushi.community.domain.model.content.*;
import com.beautysight.liurushi.community.domain.service.AuthorService;
import com.beautysight.liurushi.fundamental.domain.storage.StorageService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 与内容相关的应用逻辑
 *
 * @author chenlong
 * @since 1.0
 */
@Service
public class WorkApp {

    @Autowired
    private PictureStoryRepo pictureStoryRepo;
    @Autowired
    private PresentationRepo presentationRepo;
    @Autowired
    private ContentSectionRepo contentSectionRepo;
    @Autowired
    private StorageService storageService;
    @Autowired
    private AuthorService authorService;

    public void publishWork(PublishWorkCommand command) {
        Map<String, ContentSection> contentSections = save(command.contentSections());
        PictureStory pictureStory = command.pictureStory.toPictureStory();
        Presentation presentation = command.presentation.toPresentation();
        setCover(pictureStory, contentSections, command);
        setContentSections(pictureStory, contentSections, command.pictureStory.shots);
        setContentSections(presentation, contentSections, command.presentation.slides);
        PictureStory savedPictureStory = pictureStoryRepo.save(pictureStory);
        presentation.setId(savedPictureStory.id());
        presentationRepo.save(presentation);
    }

    public WorkPresentation getWorkBy(String workId) {
        PictureStory pictureStory = pictureStoryRepo.findOne(workId);
        Presentation presentation = presentationRepo.findOne(workId);

        Map<String, String> keyToDownloadUrlMapping = new HashMap<>();
        String coverPictureKey = pictureStory.cover().pictureKey();
        String downloadUrl = storageService.issueDownloadUrl(coverPictureKey);
        keyToDownloadUrlMapping.put(coverPictureKey, downloadUrl);
        for (PictureStory.Shot shot : pictureStory.componentParts()) {
            if (shot.content() instanceof Picture) {
                Picture picture = (Picture) shot.content();
                downloadUrl = storageService.issueDownloadUrl(picture.key());
                keyToDownloadUrlMapping.put(picture.key(), downloadUrl);
            }
        }

        return WorkPresentation.from(pictureStory, presentation, keyToDownloadUrlMapping);
    }

    public WorkProfilePresentation getPgcLatestWorkProfiles(int count) {
        return getLatestWorkProfiles(Work.Source.pgc, count);
    }

    public WorkProfilePresentation findPgcWorkProfilesInRange(String referenceWorkId, int offset) {
        return findWorkProfilesInRange(Work.Source.pgc, referenceWorkId, offset);
    }

    public WorkProfilePresentation getUgcLatestWorkProfiles(int count) {
        return getLatestWorkProfiles(Work.Source.ugc, count);
    }

    public WorkProfilePresentation findUgcWorkProfilesInRange(String referenceWorkId, int offset) {
        return findWorkProfilesInRange(Work.Source.ugc, referenceWorkId, offset);
    }

    private WorkProfilePresentation getLatestWorkProfiles(Work.Source source, int count) {
        List<WorkProfile> workProfiles = new ArrayList<>();
        List<PictureStory> theWorks = pictureStoryRepo.getLatestPictureStories(source, count);

        if (CollectionUtils.isEmpty(theWorks)) {
            return new WorkProfilePresentation(workProfiles);
        }

        for (PictureStory pictureStory : theWorks) {
            Author author = authorService.getAuthorBy(pictureStory.authorId().toString());
            String coverPictureUrl = storageService.issueDownloadUrl(pictureStory.cover().pictureKey());
            workProfiles.add(WorkProfile.from(pictureStory, coverPictureUrl, author));
        }

        return new WorkProfilePresentation(workProfiles);
    }

    private WorkProfilePresentation findWorkProfilesInRange(Work.Source source, String referenceWorkId, int offset) {
        List<WorkProfile> workProfiles = new ArrayList<>();
        List<PictureStory> theWorks = pictureStoryRepo.findPictureStoriesInRange(source, referenceWorkId, offset);

        if (CollectionUtils.isEmpty(theWorks)) {
            return new WorkProfilePresentation(workProfiles);
        }

        for (PictureStory pictureStory : theWorks) {
            Author author = authorService.getAuthorBy(pictureStory.authorId().toString());
            String coverPictureUrl = storageService.issueDownloadUrl(pictureStory.cover().pictureKey());
            workProfiles.add(WorkProfile.from(pictureStory, coverPictureUrl, author));
        }

        return new WorkProfilePresentation(workProfiles);
    }

    private Map<String, ContentSection> save(Map<String, ContentSection> newSections) {
        for (Map.Entry<String, ContentSection> entry : newSections.entrySet()) {
            ContentSection section = contentSectionRepo.save(entry.getValue());
            entry.setValue(section);
        }
        return newSections;
    }

    private void setCover(PictureStory pictureStory, Map<String, ContentSection> contentSections, PublishWorkCommand command) {
        ContentSection coverSection = contentSections.get(command.pictureStory.cover.sectionId);
        pictureStory.cover().setPicture((Picture) coverSection);
    }

    private void setContentSections(Work<? extends Control> work, Map<String, ContentSection> contentSections,
                                    List<? extends ControlDPO> controlDTOs) {
        for (int i = 0; i < controlDTOs.size(); i++) {
            String key = controlDTOs.get(i).sectionId;
            work.componentParts().get(i).setContentSection(contentSections.get(key));
        }
    }

}
