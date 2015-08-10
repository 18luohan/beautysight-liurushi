/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.app;

import com.beautysight.liurushi.common.ex.IllegalParamException;
import com.beautysight.liurushi.community.app.command.FindWorkProfilesInRangeCommand;
import com.beautysight.liurushi.community.app.command.PublishWorkCommand;
import com.beautysight.liurushi.community.app.dpo.ControlDPO;
import com.beautysight.liurushi.community.app.presentation.PublishWorkPresentation;
import com.beautysight.liurushi.community.app.presentation.WorkPresentation;
import com.beautysight.liurushi.community.app.presentation.WorkProfilePresentation;
import com.beautysight.liurushi.community.domain.model.work.*;
import com.beautysight.liurushi.community.domain.model.work.cs.ContentSection;
import com.beautysight.liurushi.community.domain.model.work.cs.ContentSectionRepo;
import com.beautysight.liurushi.community.domain.model.work.cs.Picture;
import com.beautysight.liurushi.community.domain.model.work.draft.PublishingWork;
import com.beautysight.liurushi.community.domain.model.work.draft.PublishingWorkRepo;
import com.beautysight.liurushi.community.domain.model.work.picstory.PictureStory;
import com.beautysight.liurushi.community.domain.model.work.picstory.Shot;
import com.beautysight.liurushi.community.domain.model.work.present.Presentation;
import com.beautysight.liurushi.community.domain.service.AuthorService;
import com.beautysight.liurushi.fundamental.app.NotifyPicUploadedCommand;
import com.beautysight.liurushi.fundamental.domain.storage.StorageService;
import com.google.common.base.Optional;
import org.apache.commons.collections.CollectionUtils;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(WorkApp.class);

    @Autowired
    private PublishingWorkRepo publishingWorkRepo;
    @Autowired
    private WorkRepo workRepo;
    @Autowired
    private ContentSectionRepo contentSectionRepo;
    @Autowired
    private StorageService storageService;
    @Autowired
    private AuthorService authorService;

    public PublishWorkPresentation publishWork(PublishWorkCommand command) {
        Map<String, ContentSection> savedContentSections = saveContentSections(command.contentSectionsMap());
        PublishingWork publishingWork = translateToPublishingWork(command, savedContentSections);
        publishingWorkRepo.save(publishingWork);
        String uploadToken = storageService.issueUploadToken();
        return new PublishWorkPresentation(publishingWork.idAsStr(), uploadToken, savedContentSections);
    }

    public void onPicSectionUploaded(String publishingWorkId, String contentSectionId, NotifyPicUploadedCommand command) {
        ContentSection contentSection = contentSectionRepo.findOne(contentSectionId);
        if (!contentSection.isImage()) {
            throw new IllegalParamException(
                    "Content section is not picture, publishingWorkId:%s, contentSectionId:%s",
                    publishingWorkId, contentSectionId);
        }

        Picture picture = (Picture) contentSection;
        if (picture.isUploaded()) {
            throw new IllegalParamException(
                    "Picture is already uploaded, publishingWorkId:%s, contentSectionId:%s",
                    publishingWorkId, contentSectionId);
        }

        picture.setHash(command.hash);
        contentSectionRepo.merge(picture);
        logger.info("Set picture to uploaded, publishingWorkId:{}, contentSectionId:{}",
                publishingWorkId, contentSectionId);

        PublishingWork workOnlyWithContentSections = publishingWorkRepo.getAllContentSectionsIn(publishingWorkId);
        boolean isAllUploaded = true;
        for (ContentSection section : workOnlyWithContentSections.contentSections()) {
            if (section.isImage() && !((Picture) section).isUploaded()) {
                isAllUploaded = false;
            }
        }

        if (isAllUploaded) {
            PublishingWork publishingWork = publishingWorkRepo.findOne(publishingWorkId);
            Work theWork = workRepo.save(publishingWork.transformToWork());
            logger.info("All pictures uploaded, so transform work draft to work, publishingWorkId:{}, workId:{}",
                    publishingWorkId, theWork.idAsStr());
        }
    }

    public WorkPresentation getWorkBy(String workId) {
        Work work = workRepo.findOne(workId);
        PictureStory pictureStory = work.pictureStory();
        Presentation presentation = work.presentation();

        Map<String, String> keyToDownloadUrlMapping = new HashMap<>();
        String coverPictureKey = pictureStory.cover().pictureKey();
        String downloadUrl = storageService.issueDownloadUrl(coverPictureKey);
        keyToDownloadUrlMapping.put(coverPictureKey, downloadUrl);
        for (Shot shot : pictureStory.controls()) {
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

    public WorkProfilePresentation findPgcWorkProfilesInRange(FindWorkProfilesInRangeCommand command) {
        return findWorkProfilesInRange(Work.Source.pgc, command);
    }

    public WorkProfilePresentation getUgcLatestWorkProfiles(int count) {
        return getLatestWorkProfiles(Work.Source.ugc, count);
    }

    public WorkProfilePresentation findUgcWorkProfilesInRange(FindWorkProfilesInRangeCommand command) {
        return findWorkProfilesInRange(Work.Source.ugc, command);
    }

    private WorkProfilePresentation getLatestWorkProfiles(Work.Source source, int count) {
        List<WorkProfile> workProfiles = new ArrayList<>();
        List<Work> theWorks = workRepo.getLatestWorks(source, count);

        if (CollectionUtils.isEmpty(theWorks)) {
            return new WorkProfilePresentation(workProfiles);
        }

        for (Work work : theWorks) {
            Author author = authorService.getAuthorBy(work.authorId().toString());
            String coverPictureUrl = storageService.issueDownloadUrl(work.cover().pictureKey());
            workProfiles.add(WorkProfile.from(work, coverPictureUrl, author));
        }

        return new WorkProfilePresentation(workProfiles);
    }

    private WorkProfilePresentation findWorkProfilesInRange(Work.Source source, FindWorkProfilesInRangeCommand command) {
        List<WorkProfile> workProfiles = new ArrayList<>();

        List<Work> theWorks = workRepo.findWorksInRange(
                source, Optional.fromNullable(command.referenceWork), command.offset, command.direction);

        if (CollectionUtils.isEmpty(theWorks)) {
            return new WorkProfilePresentation(workProfiles);
        }

        for (Work work : theWorks) {
            Author author = authorService.getAuthorBy(work.authorId().toString());
            String coverPictureUrl = storageService.issueDownloadUrl(work.cover().pictureKey());
            workProfiles.add(WorkProfile.from(work, coverPictureUrl, author));
        }

        return new WorkProfilePresentation(workProfiles);
    }

    private Map<String, ContentSection> saveContentSections(Map<String, ContentSection> newSections) {
        for (Map.Entry<String, ContentSection> entry : newSections.entrySet()) {
            if (entry.getValue() instanceof Picture) {
                Picture pic = (Picture) entry.getValue();
                pic.setKey(new ObjectId().toHexString());
            }
            ContentSection section = contentSectionRepo.save(entry.getValue());
            entry.setValue(section);
        }
        return newSections;
    }

    private PublishingWork translateToPublishingWork(PublishWorkCommand command, Map<String, ContentSection> contentSections) {
        PictureStory pictureStory = command.pictureStory.toPictureStory();
        Presentation presentation = command.presentation.toPresentation();
        setCover(pictureStory, contentSections, command);
        setContentSections(pictureStory, contentSections, command.pictureStory.shots);
        setContentSections(presentation, contentSections, command.presentation.slides);
        return new PublishingWork(pictureStory, presentation, command.author, new ArrayList<>(contentSections.values()));
    }

    private void setCover(PictureStory pictureStory, Map<String, ContentSection> contentSections, PublishWorkCommand command) {
        ContentSection coverSection = contentSections.get(command.pictureStory.cover.sectionId);
        pictureStory.cover().setPicture((Picture) coverSection);
    }

    private void setContentSections(WorkPart<? extends Control> workPart, Map<String, ContentSection> contentSections,
                                    List<? extends ControlDPO> controlDTOs) {
        for (int i = 0; i < controlDTOs.size(); i++) {
            String key = controlDTOs.get(i).sectionId;
            workPart.controls().get(i).setContentSection(contentSections.get(key));
        }
    }

}
