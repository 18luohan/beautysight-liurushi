/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.app;

import com.beautysight.liurushi.common.domain.CountResult;
import com.beautysight.liurushi.common.domain.Range;
import com.beautysight.liurushi.common.ex.BusinessException;
import com.beautysight.liurushi.common.ex.IllegalParamException;
import com.beautysight.liurushi.community.app.command.PublishWorkCommand;
import com.beautysight.liurushi.community.app.command.WorkQueryInRangeCommand;
import com.beautysight.liurushi.community.app.dpo.ContentSectionPayload;
import com.beautysight.liurushi.community.app.dpo.ControlPayload;
import com.beautysight.liurushi.community.app.presentation.PublishWorkPresentation;
import com.beautysight.liurushi.community.app.presentation.WorkProfileList;
import com.beautysight.liurushi.community.app.presentation.WorkVM;
import com.beautysight.liurushi.community.domain.service.AuthorService;
import com.beautysight.liurushi.community.domain.service.LikeService;
import com.beautysight.liurushi.community.domain.work.*;
import com.beautysight.liurushi.community.domain.work.cs.ContentSection;
import com.beautysight.liurushi.community.domain.work.cs.ContentSectionRepo;
import com.beautysight.liurushi.community.domain.work.ContentType;
import com.beautysight.liurushi.community.domain.work.cs.Rich;
import com.beautysight.liurushi.community.domain.work.draft.PublishingWork;
import com.beautysight.liurushi.community.domain.work.draft.PublishingWorkRepo;
import com.beautysight.liurushi.community.domain.work.layout.BlockLocator;
import com.beautysight.liurushi.community.domain.work.picstory.Shot;
import com.beautysight.liurushi.community.domain.work.picstory.Story;
import com.beautysight.liurushi.community.domain.work.present.Presentation;
import com.beautysight.liurushi.fundamental.app.NotifyRichContentUploadedCommand;
import com.beautysight.liurushi.fundamental.domain.storage.FileMetadata;
import com.beautysight.liurushi.fundamental.domain.storage.FileMetadataRepo;
import com.beautysight.liurushi.fundamental.domain.storage.FileMetadataService;
import com.beautysight.liurushi.fundamental.domain.storage.StorageService;
import com.google.common.base.Optional;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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

    public void notifyRichContentUploaded(String publishingWorkId, String fileId, NotifyRichContentUploadedCommand command) {
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
        Map<String, String> keyToDownloadUrlMapping = mapFileKeyToUrl(work);

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
        ContentSectionPayload.RichPayload coverContentPayload =
                workService.toCoverContentPayload(workProfile.cover().getContent(), intThumbnailSpec);
        Author author = authorService.getAuthorBy(workProfile.authorId());
        WorkProfileVM result = new WorkProfileVM(workProfile, coverContentPayload, author);

        if (loginUserId.isPresent()) {
            result.setIsLiked(likeService.isWorkLikedByUser(workId, loginUserId.get()));
        }
        return result;
    }

    public WorkVM shareWork(String workId) {
        Work work = workRepo.getFullWork(workId);
        Map<String, String> keyToDownloadUrlMapping = mapFileKeyToUrl(work);

        Story Story = work.story();
        BlockLocator locator = Story.layout().generateBlockLocator();
        for (int i = 0; i < Story.controls().size(); i++) {
            Shot shot = Story.controls().get(i);
            shot.calculatePosition(locator);
        }

        return WorkVM.from(work, keyToDownloadUrlMapping);
    }

    public WorkProfileList findPgcWorkProfilesIn(WorkQueryInRangeCommand command) {
        return workService.findWorkProfilesInRange(Work.Source.pgc, command.range, command.loginUserId, command.thumbnailSpec, command.supportedContentTypes);
    }

    public WorkProfileList findUgcWorkProfilesIn(WorkQueryInRangeCommand command) {
        return workService.findWorkProfilesInRange(Work.Source.ugc, command.range, command.loginUserId, command.thumbnailSpec, command.supportedContentTypes);
    }

    public WorkProfileList findAuthorWorksIn(WorkQueryInRangeCommand command) {
        List<WorkProfileVM> workProfiles = new ArrayList<>();
        List<Work> theWorks = workRepo.findAuthorWorkProfilesIn(command);

        if (CollectionUtils.isEmpty(theWorks)) {
            return new WorkProfileList(workProfiles);
        }

        for (Work work : theWorks) {
            ContentSectionPayload.RichPayload coverContentPayload =
                    workService.toCoverContentPayload(work.cover().getContent(), command.thumbnailSpec);
            workProfiles.add(new WorkProfileVM(work, coverContentPayload));
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
        workService.select(workId);
    }

    public void cancelSelectWork(String workId) {
        workService.cancelSelect(workId);
    }

    public void ordinaryWork(String workId) {
        workService.ordinary(workId);
    }

    public void cancelOrdinaryWork(String workId) {
        workService.cancelOrdinary(workId);
    }

    public void discardWork(String workId) {
        discardWork(workId, Optional.<String>absent());
    }

    public void discardWork(String workId, Optional<String> loginUserId) {
        Optional<Work> work = workRepo.get(workId);
        if (work.isPresent()) {
            // 检查是否是当前用户的作品
            if (loginUserId.isPresent() && !work.get().authorId().equals(loginUserId.get())) {
                throw new BusinessException("current user(%s) has no privilege to delete work(%s) of author(%s)",
                        loginUserId.get(), workId, work.get().authorId());
            }

            discardedWorkRepo.save(new DiscardedWork(work.get()));
            workRepo.delete(work.get().id());
            authorService.increaseWorkNumBy(-1, work.get().authorId());
        }
    }

    public void cancelDiscardWork(String workId) {
        Optional<DiscardedWork> discardedWork = discardedWorkRepo.get(workId);
        if (discardedWork.isPresent()) {
            workRepo.save(discardedWork.get().transformToWork());
            discardedWorkRepo.delete(discardedWork.get().id());
            authorService.increaseWorkNumBy(1, discardedWork.get().authorId());
        }
    }

    public WorkVM getFullWorkForExamine(String workId) {
        Work work;
        if (discardedWorkRepo.get(workId).isPresent()) {
            work = discardedWorkRepo.getFullWork(workId);
        } else {
            work = workRepo.getFullWork(workId);
        }

        Map<String, String> keyToDownloadUrlMapping = mapFileKeyToUrl(work);

        Author author = authorService.getAuthorBy(work.authorId());
        return WorkVM.from(work, keyToDownloadUrlMapping, author);
    }

    public CountResult countUgcWorksByPresentPriority(Work.PresentPriority presentPriority) {
        return workRepo.countWorksByPresentPriority(presentPriority);
    }

    private Map<String, ContentSection> saveContentSections(Map<String, ContentSection> newSections) {
        for (Map.Entry<String, ContentSection> entry : newSections.entrySet()) {
            if (entry.getValue() instanceof Rich) {
                Rich richContent = (Rich) entry.getValue();
                richContent.setFile(fileMetadataService.createOneLogicFile(
                        richContent.type().toString(), FileMetadata.BizCategory.work));
            }
            ContentSection section = contentSectionRepo.save(entry.getValue());
            entry.setValue(section);
        }
        return newSections;
    }

    private PublishingWork translateToPublishingWork(PublishWorkCommand command, Map<String, ContentSection> contentSections) {
        Story Story = command.story.toPictureStory();
        Presentation presentation = command.presentation.toPresentation();

        setWorkCover(Story, contentSections, command);
        setContentSections(Story, contentSections, command.story.shots);
        setContentSections(presentation, contentSections, command.presentation.slides);

        List<FileMetadata> files = new ArrayList<>(contentSections.size());
        List<ContentType> contentTypes = new ArrayList<>(5);
        for (ContentSection section : contentSections.values()) {
            if (!contentTypes.contains(section.type())) {
                contentTypes.add(section.type());
            }
            if (section instanceof Rich) {
                Rich rich = (Rich) section;
                files.add(rich.file());
            }
        }

        Author author = authorService.getAuthorBy(command.authorId);
        PublishingWork result = new PublishingWork(command.title, command.subtitle, Story, presentation, author, files);
        result.setContentTypes(contentTypes);
        return result;
    }

    private void setWorkCover(Story Story, Map<String, ContentSection> contentSections, PublishWorkCommand command) {
        ContentSection coverSection = contentSections.get(command.story.cover.sectionId);
        Story.cover().setContent((Rich) coverSection);
    }

    private void setContentSections(WorkPart<? extends Control> workPart, Map<String, ContentSection> contentSections,
                                    List<? extends ControlPayload> controlDTOs) {
        for (int i = 0; i < controlDTOs.size(); i++) {
            String key = controlDTOs.get(i).sectionId;
            workPart.controls().get(i).setContentSection(contentSections.get(key));
        }
    }

    private Map<String, String> mapFileKeyToUrl(Work work) {
        Map<String, String> keyToDownloadUrlMapping = new HashMap<>();
        String coverKey = work.cover().contentFileKey();
        String coverUrl = storageService.downloadUrl(coverKey);
        keyToDownloadUrlMapping.put(coverKey, coverUrl);

        String fileUrl;
        for (Shot shot : work.story().controls()) {
            if (shot.content() instanceof Rich) {
                Rich rich = (Rich) shot.content();
                fileUrl = storageService.downloadUrl(rich.fileKey());
                keyToDownloadUrlMapping.put(rich.fileKey(), fileUrl);
            }
        }

        return keyToDownloadUrlMapping;
    }

}