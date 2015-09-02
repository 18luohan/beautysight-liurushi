/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.app;

import com.beautysight.liurushi.common.domain.Range;
import com.beautysight.liurushi.common.ex.IllegalParamException;
import com.beautysight.liurushi.community.app.command.PublishWorkCommand;
import com.beautysight.liurushi.community.app.command.AuthorWorksRange;
import com.beautysight.liurushi.community.app.dpo.ControlDPO;
import com.beautysight.liurushi.community.app.dpo.PictureStoryDPO;
import com.beautysight.liurushi.community.app.presentation.PublishWorkPresentation;
import com.beautysight.liurushi.community.app.presentation.WorkPresentation;
import com.beautysight.liurushi.community.app.presentation.WorkProfilesPresentation;
import com.beautysight.liurushi.community.domain.model.work.*;
import com.beautysight.liurushi.community.domain.model.work.cs.ContentSection;
import com.beautysight.liurushi.community.domain.model.work.cs.ContentSectionRepo;
import com.beautysight.liurushi.community.domain.model.work.cs.Picture;
import com.beautysight.liurushi.community.domain.model.work.draft.PublishingWork;
import com.beautysight.liurushi.community.domain.model.work.draft.PublishingWorkRepo;
import com.beautysight.liurushi.community.domain.model.work.layout.BlockLocator;
import com.beautysight.liurushi.community.domain.model.work.picstory.PictureStory;
import com.beautysight.liurushi.community.domain.model.work.picstory.Shot;
import com.beautysight.liurushi.community.domain.model.work.present.Presentation;
import com.beautysight.liurushi.community.domain.service.AuthorService;
import com.beautysight.liurushi.fundamental.app.NotifyPicUploadedCommand;
import com.beautysight.liurushi.fundamental.domain.appconfig.AppConfig;
import com.beautysight.liurushi.fundamental.domain.appconfig.AppConfigService;
import com.beautysight.liurushi.fundamental.domain.appconfig.IntegerVal;
import com.beautysight.liurushi.fundamental.domain.storage.FileMetadata;
import com.beautysight.liurushi.fundamental.domain.storage.FileMetadataRepo;
import com.beautysight.liurushi.fundamental.domain.storage.FileMetadataService;
import com.beautysight.liurushi.fundamental.domain.storage.StorageService;
import org.apache.commons.collections.CollectionUtils;
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
    @Autowired
    private FileMetadataService fileMetadataService;
    @Autowired
    private FileMetadataRepo fileMetadataRepo;
    @Autowired
    private AppConfigService appConfigService;

    public PublishWorkPresentation publishWork(PublishWorkCommand command) {
        Map<String, ContentSection> savedContentSections = saveContentSections(command.contentSectionsMap());
        PublishingWork publishingWork = translateToPublishingWork(command, savedContentSections);
        publishingWorkRepo.save(publishingWork);
        String uploadToken = storageService.issueUploadToken();
        return new PublishWorkPresentation(publishingWork.idAsStr(), uploadToken, savedContentSections);
    }

    public void onPicSectionUploaded(String publishingWorkId, String fileId, NotifyPicUploadedCommand command) {
        FileMetadata theFile = fileMetadataRepo.findOne(fileId);

        if (theFile.isUploaded()) {
            throw new IllegalParamException(
                    "Picture is already uploaded, publishingWorkId:%s, fileId:%s",
                    publishingWorkId, fileId);
        }

        theFile.setHash(command.hash);
        fileMetadataRepo.merge(theFile);
        logger.info("Set picture to uploaded, publishingWorkId:{}, fileId:{}",
                publishingWorkId, fileId);

        boolean isAllUploaded = true;
        PublishingWork workOnlyWithFiles = publishingWorkRepo.getAllFilesIn(publishingWorkId);
        for (FileMetadata file : workOnlyWithFiles.files()) {
            if (!file.isUploaded()) {
                isAllUploaded = false;
                break;
            }
        }

        if (isAllUploaded) {
            PublishingWork publishingWork = publishingWorkRepo.findOne(publishingWorkId);
            Work theWork = workRepo.save(publishingWork.transformToWork());
            logger.info("All pictures uploaded, so transform publishing-work to work, publishingWorkId:{}, workId:{}",
                    publishingWorkId, theWork.idAsStr());
            publishingWorkRepo.delete(publishingWork.id());
            logger.info("Transformed publishing-work to work, so delete publishing-work, publishingWorkId:{}, workId:{}",
                    publishingWorkId, theWork.idAsStr());
            authorService.increaseWorkNumBy(1, theWork.authorId().toHexString());
            logger.info("Increased workNum of author({}) by 1", theWork.authorId().toHexString());
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

    public PictureStoryDPO h5SharingOf(String workId) {
        Work workOnlyWithPictureStory = workRepo.getPictureStoryOf(workId);
        PictureStory pictureStory = workOnlyWithPictureStory.pictureStory();

        Map<String, String> keyToDownloadUrlMapping = new HashMap<>();
        String coverPictureKey = pictureStory.cover().pictureKey();
        String downloadUrl = storageService.issueDownloadUrl(coverPictureKey);
        keyToDownloadUrlMapping.put(coverPictureKey, downloadUrl);

        IntegerVal shotsNum = appConfigService.getItemValue(AppConfig.ItemName.sharing_h5_shots_num);
        BlockLocator locator = pictureStory.layout().generateBlockLocator();
        int endIndex = 0;
        for (int i = 0; i < pictureStory.controls().size(); i++) {
            if ((i + 1) > shotsNum.val()) {
                endIndex = i;
                break;
            }

            Shot shot = pictureStory.controls().get(i);
            shot.calculatePosition(locator);
            if (shot.content() instanceof Picture) {
                Picture picture = (Picture) shot.content();
                downloadUrl = storageService.issueDownloadUrl(picture.key());
                keyToDownloadUrlMapping.put(picture.key(), downloadUrl);
            }
        }
        pictureStory.sliceShots(0, endIndex);

        return PictureStoryDPO.from(pictureStory, keyToDownloadUrlMapping);
    }

    public WorkProfilesPresentation getPgcLatestWorkProfiles(int count) {
        return getLatestWorkProfiles(Work.Source.pgc, count);
    }

    public WorkProfilesPresentation findPgcWorkProfilesIn(Range range) {
        return findWorkProfilesInRange(Work.Source.pgc, range);
    }

    public WorkProfilesPresentation getUgcLatestWorkProfiles(int count) {
        return getLatestWorkProfiles(Work.Source.ugc, count);
    }

    public WorkProfilesPresentation findUgcWorkProfilesIn(Range range) {
        return findWorkProfilesInRange(Work.Source.ugc, range);
    }

    public WorkProfilesPresentation findAuthorWorksIn(AuthorWorksRange range) {
        List<WorkProfile> workProfiles = new ArrayList<>();
        List<Work> theWorks = workRepo.findAuthorWorksIn(range);

        if (CollectionUtils.isEmpty(theWorks)) {
            return new WorkProfilesPresentation(workProfiles);
        }

        for (Work work : theWorks) {
            String coverPictureUrl = storageService.issueDownloadUrl(work.cover().pictureKey());
            workProfiles.add(WorkProfile.from(work, coverPictureUrl));
        }

        return new WorkProfilesPresentation(workProfiles);
    }

    private WorkProfilesPresentation getLatestWorkProfiles(Work.Source source, int count) {
        List<WorkProfile> workProfiles = new ArrayList<>();
        List<Work> theWorks = workRepo.getLatestWorks(source, count);

        if (CollectionUtils.isEmpty(theWorks)) {
            return new WorkProfilesPresentation(workProfiles);
        }

        for (Work work : theWorks) {
            Author author = authorService.getAuthorBy(work.authorId().toString());
            String coverPictureUrl = storageService.issueDownloadUrl(work.cover().pictureKey());
            workProfiles.add(WorkProfile.from(work, coverPictureUrl, author));
        }

        return new WorkProfilesPresentation(workProfiles);
    }

    private WorkProfilesPresentation findWorkProfilesInRange(Work.Source source, Range range) {
        List<WorkProfile> workProfiles = new ArrayList<>();

        List<Work> theWorks = workRepo.findWorksInRange(source, range);

        if (CollectionUtils.isEmpty(theWorks)) {
            return new WorkProfilesPresentation(workProfiles);
        }

        for (Work work : theWorks) {
            Author author = authorService.getAuthorBy(work.authorId().toString());
            String coverPictureUrl = storageService.issueDownloadUrl(work.cover().pictureKey());
            workProfiles.add(WorkProfile.from(work, coverPictureUrl, author));
        }

        return new WorkProfilesPresentation(workProfiles);
    }

    private Map<String, ContentSection> saveContentSections(Map<String, ContentSection> newSections) {
        for (Map.Entry<String, ContentSection> entry : newSections.entrySet()) {
            if (entry.getValue() instanceof Picture) {
                Picture pic = (Picture) entry.getValue();
                pic.setFile(fileMetadataService.createOneLogicFile(FileMetadata.Type.image));
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

        List<FileMetadata> files = new ArrayList<>(contentSections.size());
        for (ContentSection section : contentSections.values()) {
            if (section instanceof Picture) {
                Picture pic = (Picture) section;
                files.add(pic.file());
            }
        }

        return new PublishingWork(pictureStory, presentation, command.author, files);
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