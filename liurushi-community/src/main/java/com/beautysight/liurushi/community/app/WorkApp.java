/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.app;

import com.beautysight.liurushi.common.domain.Range;
import com.beautysight.liurushi.common.ex.IllegalParamException;
import com.beautysight.liurushi.community.app.command.AuthorWorksRange;
import com.beautysight.liurushi.community.app.command.PublishWorkCommand;
import com.beautysight.liurushi.community.app.dpo.ControlPayload;
import com.beautysight.liurushi.community.app.presentation.PublishWorkPresentation;
import com.beautysight.liurushi.community.app.presentation.WorkProfileList;
import com.beautysight.liurushi.community.app.presentation.WorkVM;
import com.beautysight.liurushi.community.domain.model.like.Like;
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
import com.beautysight.liurushi.community.domain.service.LikeService;
import com.beautysight.liurushi.fundamental.app.NotifyPicUploadedCommand;
import com.beautysight.liurushi.fundamental.domain.appconfig.AppConfig;
import com.beautysight.liurushi.fundamental.domain.appconfig.AppConfigService;
import com.beautysight.liurushi.fundamental.domain.appconfig.IntegerVal;
import com.beautysight.liurushi.fundamental.domain.storage.FileMetadata;
import com.beautysight.liurushi.fundamental.domain.storage.FileMetadataRepo;
import com.beautysight.liurushi.fundamental.domain.storage.FileMetadataService;
import com.beautysight.liurushi.fundamental.domain.storage.StorageService;
import com.google.common.base.Optional;
import com.google.common.base.Supplier;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimaps;
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
            authorService.increaseWorkNumBy(1, theWork.authorId());
            logger.info("Increased workNum of author({}) by 1", theWork.authorId());
        }
    }

    public WorkVM getFullWorkBy(String workId, Optional<String> loginUserId) {
        Work work = workRepo.findOne(workId);

        Map<String, String> keyToDownloadUrlMapping = new HashMap<>();
        String coverPictureKey = work.pictureStory().cover().pictureKey();
        String downloadUrl = storageService.issueDownloadUrl(coverPictureKey);
        keyToDownloadUrlMapping.put(coverPictureKey, downloadUrl);
        for (Shot shot : work.pictureStory().controls()) {
            if (shot.content() instanceof Picture) {
                Picture picture = (Picture) shot.content();
                downloadUrl = storageService.issueDownloadUrl(picture.key());
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

    public WorkProfileVM getWorkProfileBy(String workId, Optional<String> loginUserId) {
        Work workProfile = workRepo.getWorkProfile(workId);
        String coverPictureKey = workProfile.pictureStory().cover().pictureKey();
        String coverPictureUrl = storageService.issueDownloadUrl(coverPictureKey);
        Author author = authorService.getAuthorBy(workProfile.authorId());
        WorkProfileVM result = new WorkProfileVM(workProfile, coverPictureUrl, author);

        if (loginUserId.isPresent()) {
            result.setIsLiked(likeService.isWorkLikedByUser(workId, loginUserId.get()));
        }
        return result;
    }

    public WorkVM shareWork(String workId) {
        Work workOnlyWithPictureStory = workRepo.getWorkOnlyWithPictureStory(workId);
        PictureStory pictureStory = workOnlyWithPictureStory.pictureStory();

        Map<String, String> keyToDownloadUrlMapping = new HashMap<>();
        String coverPictureKey = pictureStory.cover().pictureKey();
        String downloadUrl = storageService.issueDownloadUrl(coverPictureKey);
        keyToDownloadUrlMapping.put(coverPictureKey, downloadUrl);

        IntegerVal shotsNum = appConfigService.getItemValue(AppConfig.ItemName.sharing_h5_shots_num);
        BlockLocator locator = pictureStory.layout().generateBlockLocator();

        int count = 0;
        for (int i = 0; i < pictureStory.controls().size() && count < shotsNum.val(); i++, count++) {
            Shot shot = pictureStory.controls().get(i);
            shot.calculatePosition(locator);
            if (shot.content() instanceof Picture) {
                Picture picture = (Picture) shot.content();
                downloadUrl = storageService.issueDownloadUrl(picture.key());
                keyToDownloadUrlMapping.put(picture.key(), downloadUrl);
            }
        }
        pictureStory.sliceShots(0, count);

        return WorkVM.from(workOnlyWithPictureStory, keyToDownloadUrlMapping);
    }

    public WorkProfileList getPgcLatestWorkProfiles(int count, Optional<String> loginUserId) {
        Range range = new Range(count, Range.OffsetDirection.before);
        return findWorkProfilesInRange(Work.Source.pgc, range, loginUserId);
    }

    public WorkProfileList findPgcWorkProfilesIn(Range range, Optional<String> loginUserId) {
        return findWorkProfilesInRange(Work.Source.pgc, range, loginUserId);
    }

    public WorkProfileList getUgcLatestWorkProfiles(int count, Optional<String> loginUserId) {
        Range range = new Range(count, Range.OffsetDirection.before);
        return findWorkProfilesInRange(Work.Source.ugc, range, loginUserId);
    }

    public WorkProfileList findUgcWorkProfilesIn(Range range, Optional<String> loginUserId) {
        return findWorkProfilesInRange(Work.Source.ugc, range, loginUserId);
    }

    public WorkProfileList findAuthorWorksIn(AuthorWorksRange range) {
        List<WorkProfileVM> workProfiles = new ArrayList<>();
        List<Work> theWorks = workRepo.findAuthorWorksIn(range);

        if (CollectionUtils.isEmpty(theWorks)) {
            return new WorkProfileList(workProfiles);
        }

        for (Work work : theWorks) {
            String coverPictureUrl = storageService.issueDownloadUrl(work.cover().pictureKey());
            workProfiles.add(new WorkProfileVM(work, coverPictureUrl));
        }

        return new WorkProfileList(workProfiles);
    }

    private WorkProfileList findWorkProfilesInRange(Work.Source source, Range range, Optional<String> loginUserId) {
        List<WorkProfileVM> workProfiles = new ArrayList<>();

        List<Work> works = workRepo.findWorkProfilesInRange(source, range);
        if (CollectionUtils.isEmpty(works)) {
            return new WorkProfileList(workProfiles);
        }

        Set<String> authorIds = new HashSet<>(works.size());
        List<String> workIds = new ArrayList<>(works.size());
        for (Work work : works) {
            authorIds.add(work.authorId());
            workIds.add(work.idAsStr());
        }

        ListMultimap<String, Work> authorToWorksMap = Multimaps.newListMultimap(
                Maps.<String, Collection<Work>>newHashMap(),
                new Supplier<List<Work>>() {
                    public List<Work> get() {
                        return Lists.newArrayList();
                    }
                });
        for (Work work : works) {
            authorToWorksMap.put(work.authorId(), work);
        }

        List<Author> authors = authorService.getAuthorsBy(authorIds);
        Map<String, WorkProfileVM> workIdToWorkProfileMap = new HashMap<>(works.size());
        for (Author author : authors) {
            List<Work> authorWorks = authorToWorksMap.get(author.id);
            for (Work work : authorWorks) {
                String coverPictureUrl = storageService.issueDownloadUrl(work.cover().pictureKey());
                WorkProfileVM workProfile = new WorkProfileVM(work, coverPictureUrl, author);
                workIdToWorkProfileMap.put(work.idAsStr(), workProfile);
            }
        }

        if (loginUserId.isPresent()) {
            List<Like> likes = likeService.findUserLikesOfWorks(loginUserId.get(), workIds);
            for (Like like : likes) {
                workIdToWorkProfileMap.get(like.workId()).setIsLiked(Boolean.TRUE);
            }
        }

        // 确保与其他对象集合进行连接后列表顺序仍与数据库返回的顺序一样
        for (Work work : works) {
            workProfiles.add(workIdToWorkProfileMap.get(work.idAsStr()));
        }

        return new WorkProfileList(workProfiles);
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