/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.app;

import com.beautysight.liurushi.common.domain.Range;
import com.beautysight.liurushi.common.ex.IllegalParamException;
import com.beautysight.liurushi.community.app.command.AuthorWorksRange;
import com.beautysight.liurushi.community.app.command.PublishWorkCommand;
import com.beautysight.liurushi.community.app.command.WorkQueryInRangeCommand;
import com.beautysight.liurushi.community.app.dpo.ControlPayload;
import com.beautysight.liurushi.community.app.presentation.PublishWorkPresentation;
import com.beautysight.liurushi.community.app.presentation.WorkProfileList;
import com.beautysight.liurushi.community.app.presentation.WorkVM;
import com.beautysight.liurushi.community.domain.service.AuthorService;
import com.beautysight.liurushi.community.domain.service.LikeService;
import com.beautysight.liurushi.community.domain.work.*;
import com.beautysight.liurushi.community.domain.work.cs.ContentSection;
import com.beautysight.liurushi.community.domain.work.cs.ContentSectionRepo;
import com.beautysight.liurushi.community.domain.work.cs.Picture;
import com.beautysight.liurushi.community.domain.work.draft.PublishingWork;
import com.beautysight.liurushi.community.domain.work.draft.PublishingWorkRepo;
import com.beautysight.liurushi.community.domain.work.picstory.PictureStory;
import com.beautysight.liurushi.community.domain.work.picstory.Shot;
import com.beautysight.liurushi.community.domain.work.present.Presentation;
import com.beautysight.liurushi.fundamental.app.NotifyPicUploadedCommand;
import com.beautysight.liurushi.fundamental.domain.appconfig.AppConfigService;
import com.beautysight.liurushi.fundamental.domain.storage.*;
import com.google.common.base.Optional;
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
    private DiscardedWorkRepo discardedWorkRepo;

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
    @Autowired
    private LikeService likeService;
    @Autowired
    private WorkService workService;

    public PublishWorkPresentation publishWork(PublishWorkCommand command) {
        Map<String, ContentSection> savedContentSections = saveContentSections(command.contentSectionsMap());
        PublishingWork publishingWork = translateToPublishingWork(command, savedContentSections);
        publishingWorkRepo.save(publishingWork);
        String uploadToken = storageService.issueUploadToken();
        return new PublishWorkPresentation(publishingWork.idStr(), uploadToken, savedContentSections);
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
                    publishingWorkId, theWork.idStr());
            publishingWorkRepo.delete(publishingWork.id());
            logger.info("Transformed publishing-work to work, so delete publishing-work, publishingWorkId:{}, workId:{}",
                    publishingWorkId, theWork.idStr());
            authorService.increaseWorkNumBy(1, theWork.authorId());
            logger.info("Increased workNum of author({}) by 1", theWork.authorId());
        }
    }

    public WorkVM getFullWorkBy(String workId, Optional<String> loginUserId) {
        Work work = workRepo.getFullWork(workId);

        Map<String, String> keyToDownloadUrlMapping = new HashMap<>();
        String coverPictureKey = work.pictureStory().cover().pictureKey();
        String downloadUrl = storageService.downloadUrl(coverPictureKey);
        keyToDownloadUrlMapping.put(coverPictureKey, downloadUrl);
        for (Shot shot : work.pictureStory().controls()) {
            if (shot.content() instanceof Picture) {
                Picture picture = (Picture) shot.content();
                downloadUrl = storageService.downloadUrl(picture.key());
                keyToDownloadUrlMapping.put(picture.key(), downloadUrl);
            }
        }

        Boolean isLikedByLoginUser = Boolean.FALSE;
        Boolean isFavoredByLoginUser = Boolean.FALSE;
        if (loginUserId.isPresent()) {
            isLikedByLoginUser = likeService.isWorkLikedByUser(workId, loginUserId.get());
        }

        Author author = authorService.getAuthorBy(work.authorId());
        return WorkVM.from(work, keyToDownloadUrlMapping, isLikedByLoginUser, isFavoredByLoginUser, author);
    }

    public WorkProfileVM getWorkProfileBy(String workId, Optional<String> loginUserId, Optional<Integer> intThumbnailSpec) {
        Work workProfile = workRepo.getWorkProfile(workId);
        String coverPictureKey = workProfile.pictureStory().cover().pictureKey();
        String coverPictureUrl = storageService.imgDownloadUrl(coverPictureKey, intThumbnailSpec);
        Author author = authorService.getAuthorBy(workProfile.authorId());
        WorkProfileVM result = new WorkProfileVM(workProfile, coverPictureUrl, author);

        if (loginUserId.isPresent()) {
            result.setIsLiked(likeService.isWorkLikedByUser(workId, loginUserId.get()));
        }
        return result;
    }

    public WorkVM shareWork(String workId) {
        /*Work workOnlyWithPictureStory = workRepo.getWorkOnlyWithPictureStory(workId);
        PictureStory pictureStory = workOnlyWithPictureStory.pictureStory();

        Map<String, String> keyToDownloadUrlMapping = new HashMap<>();
        String coverPictureKey = pictureStory.cover().pictureKey();
        String downloadUrl = storageService.downloadUrl(coverPictureKey);
        keyToDownloadUrlMapping.put(coverPictureKey, downloadUrl);

        IntegerVal shotsNum = appConfigService.getItemValue(AppConfig.ItemName.sharing_h5_shots_num);
        BlockLocator locator = pictureStory.layout().generateBlockLocator();

        int count = 0;
        for (int i = 0; i < pictureStory.controls().size() && count < shotsNum.val(); i++, count++) {
            Shot shot = pictureStory.controls().get(i);
            shot.calculatePosition(locator);
            if (shot.content() instanceof Picture) {
                Picture picture = (Picture) shot.content();
                downloadUrl = storageService.downloadUrl(picture.key());
                keyToDownloadUrlMapping.put(picture.key(), downloadUrl);
            }
        }
        pictureStory.sliceShots(0, count);

        return WorkVM.from(workOnlyWithPictureStory, keyToDownloadUrlMapping);*/
        return this.getFullWorkBy(workId, Optional.<String>absent());
    }

    public WorkProfileList findPgcWorkProfilesIn(WorkQueryInRangeCommand command) {
        return workService.findWorkProfilesInRange(Work.Source.pgc, command.range, command.loginUserId, command.thumbnailSpec);
    }

    public WorkProfileList findUgcWorkProfilesIn(WorkQueryInRangeCommand command) {
        return workService.findWorkProfilesInRange(Work.Source.ugc, command.range, command.loginUserId, command.thumbnailSpec);
    }

    public WorkProfileList findAuthorWorksIn(AuthorWorksRange range) {
        List<WorkProfileVM> workProfiles = new ArrayList<>();
        List<Work> theWorks = workRepo.findAuthorWorkProfilesIn(range);

        if (CollectionUtils.isEmpty(theWorks)) {
            return new WorkProfileList(workProfiles);
        }

        for (Work work : theWorks) {
            String coverPictureUrl = storageService.imgDownloadUrl(
                    work.cover().pictureKey(),
                    ImgThumbnailSpec.wk300x);
            workProfiles.add(new WorkProfileVM(work, coverPictureUrl));
        }

        return new WorkProfileList(workProfiles);
    }

    public WorkProfileList findUgcWorkProfilesInRange(Range range, Work.PresentPriority presentPriority, Optional<Integer> intThumbnailSpec) {
        List<WorkProfileVM> workProfiles = new ArrayList<>();

        List<Work> works = workRepo.findUgcWorkProfilesInRange(range, presentPriority);
        if (CollectionUtils.isEmpty(works)) {
            return new WorkProfileList(workProfiles);
        }

        Map<String, WorkProfileVM> workIdToWorkProfileVMMap = workService.joinWorksWithAuthor(works, intThumbnailSpec);
        // 确保与其他对象集合进行连接后列表顺序仍与数据库返回的顺序一样
        for (Work work : works) {
            workProfiles.add(workIdToWorkProfileVMMap.get(work.idStr()));
        }

        return new WorkProfileList(workProfiles);
    }

    public WorkProfileList findDiscardedUgcWorkProfilesInRange(Range range, Optional<Integer> intThumbnailSpec) {
        List<WorkProfileVM> workProfiles = new ArrayList<>();
        List<DiscardedWork> works = discardedWorkRepo.findInRange(range);
        if (CollectionUtils.isEmpty(works)) {
            return new WorkProfileList(workProfiles);
        }

        Map<String, WorkProfileVM> workIdToWorkProfileVMMap = workService.joinWorksWithAuthor(works, intThumbnailSpec);
        // 确保与其他对象集合进行连接后列表顺序仍与数据库返回的顺序一样
        for (Work work : works) {
            workProfiles.add(workIdToWorkProfileVMMap.get(work.idStr()));
        }

        return new WorkProfileList(workProfiles);
    }

    public void selectWork(String workId) {
        workRepo.selectOrCancel(workId, Work.PresentPriority.selected);
    }

    public void cancelSelectWork(String workId) {
        workRepo.selectOrCancel(workId, Work.PresentPriority.ordinary);
    }

    public void discardWork(String workId) {
        Optional<Work> work = workRepo.get(workId);
        if (work.isPresent()) {
            discardedWorkRepo.save(new DiscardedWork(work.get()));
            workRepo.delete(work.get().id());
        }
    }

    public void cancelDiscardWork(String workId) {
        Optional<DiscardedWork> discardedWork = discardedWorkRepo.get(workId);
        if (discardedWork.isPresent()) {
            workRepo.save(discardedWork.get().transformToWork());
            discardedWorkRepo.delete(discardedWork.get().id());
        }
    }

    public WorkVM getFullWorkForExamine(String workId) {
        Work work;
        if (discardedWorkRepo.get(workId).isPresent()) {
            work = discardedWorkRepo.getFullWork(workId);
        } else {
            work = workRepo.getFullWork(workId);
        }

        Map<String, String> keyToDownloadUrlMapping = new HashMap<>();
        String coverPictureKey = work.pictureStory().cover().pictureKey();
        String downloadUrl = storageService.downloadUrl(coverPictureKey);
        keyToDownloadUrlMapping.put(coverPictureKey, downloadUrl);
        for (Shot shot : work.pictureStory().controls()) {
            if (shot.content() instanceof Picture) {
                Picture picture = (Picture) shot.content();
                downloadUrl = storageService.downloadUrl(picture.key());
                keyToDownloadUrlMapping.put(picture.key(), downloadUrl);
            }
        }

        Author author = authorService.getAuthorBy(work.authorId());
        return WorkVM.from(work, keyToDownloadUrlMapping, author);
    }

    private Map<String, ContentSection> saveContentSections(Map<String, ContentSection> newSections) {
        for (Map.Entry<String, ContentSection> entry : newSections.entrySet()) {
            if (entry.getValue() instanceof Picture) {
                Picture pic = (Picture) entry.getValue();
                pic.setFile(fileMetadataService.createOneLogicFile(FileMetadata.Type.image, FileMetadata.BizCategory.work));
            }
            ContentSection section = contentSectionRepo.save(entry.getValue());
            entry.setValue(section);
        }
        return newSections;
    }

    private PublishingWork translateToPublishingWork(PublishWorkCommand command, Map<String, ContentSection> contentSections) {
        PictureStory pictureStory = command.pictureStory.toPictureStory();
        Presentation presentation = command.presentation.toPresentation();

        setPictureStoryCover(pictureStory, contentSections, command);
        setContentSections(pictureStory, contentSections, command.pictureStory.shots);
        setContentSections(presentation, contentSections, command.presentation.slides);

        List<FileMetadata> files = new ArrayList<>(contentSections.size());
        for (ContentSection section : contentSections.values()) {
            if (section instanceof Picture) {
                Picture pic = (Picture) section;
                files.add(pic.file());
            }
        }

        Author author = authorService.getAuthorBy(command.authorId);
        return new PublishingWork(command.title, command.subtitle, pictureStory, presentation, author, files);
    }

    private void setPictureStoryCover(PictureStory pictureStory, Map<String, ContentSection> contentSections, PublishWorkCommand command) {
        ContentSection coverSection = contentSections.get(command.pictureStory.cover.sectionId);
        pictureStory.cover().setPicture((Picture) coverSection);
    }

    private void setContentSections(WorkPart<? extends Control> workPart, Map<String, ContentSection> contentSections,
                                    List<? extends ControlPayload> controlDTOs) {
        for (int i = 0; i < controlDTOs.size(); i++) {
            String key = controlDTOs.get(i).sectionId;
            workPart.controls().get(i).setContentSection(contentSections.get(key));
        }
    }

}