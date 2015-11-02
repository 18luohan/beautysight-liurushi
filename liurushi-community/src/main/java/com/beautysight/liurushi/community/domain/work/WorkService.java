/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.domain.work;

import com.beautysight.liurushi.common.domain.Range;
import com.beautysight.liurushi.common.utils.Beans;
import com.beautysight.liurushi.community.app.WorkProfileVM;
import com.beautysight.liurushi.community.app.dpo.ContentSectionPayload;
import com.beautysight.liurushi.community.app.presentation.WorkProfileList;
import com.beautysight.liurushi.community.domain.model.like.Like;
import com.beautysight.liurushi.community.domain.service.AuthorService;
import com.beautysight.liurushi.community.domain.service.LikeService;
import com.beautysight.liurushi.community.domain.work.cs.ContentSectionRepo;
import com.beautysight.liurushi.community.domain.work.cs.Rich;
import com.beautysight.liurushi.community.domain.work.draft.PublishingWorkRepo;
import com.beautysight.liurushi.fundamental.domain.appconfig.AppConfigService;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author chenlong
 * @since 1.0
 */
@Service
public class WorkService {

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

    public WorkProfileList findWorkProfilesInRange(Work.Source source, Range range, Optional<String> loginUserId,
                                                   Optional<Integer> intThumbnailSpec, List<ContentType> supportedContentTypes) {
        List<WorkProfileVM> workProfiles = new ArrayList<>();

        List<Work> works = workRepo.findWorkProfilesInRange(source, range, supportedContentTypes);
        if (CollectionUtils.isEmpty(works)) {
            return new WorkProfileList(workProfiles);
        }

        Map<String, WorkProfileVM> workIdToWorkProfileVMMap = joinWorksWithAuthor(works, intThumbnailSpec);

        List<String> workIds = new ArrayList<>(workIdToWorkProfileVMMap.keySet());
        if (loginUserId.isPresent()) {
            List<Like> likes = likeService.findUserLikesOfWorks(loginUserId.get(), workIds);
            for (Like like : likes) {
                workIdToWorkProfileVMMap.get(like.workId()).setIsLiked(Boolean.TRUE);
            }
        }

        // 确保与其他对象集合进行连接后列表顺序仍与数据库返回的顺序一样
        for (Work work : works) {
            workProfiles.add(workIdToWorkProfileVMMap.get(work.idStr()));
        }

        return new WorkProfileList(workProfiles);
    }

    public Map<String, WorkProfileVM> joinWorksWithAuthor(List<? extends Work> works, Optional<Integer> intThumbnailSpec) {
        Set<String> authorIds = new HashSet<>(works.size());
        List<String> workIds = new ArrayList<>(works.size());
        for (Work work : works) {
            authorIds.add(work.authorId());
            workIds.add(work.idStr());
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
        Map<String, WorkProfileVM> workIdToWorkProfileVMMap = new HashMap<>(works.size());
        for (Author author : authors) {
            List<Work> authorWorks = authorToWorksMap.get(author.id);
            for (Work work : authorWorks) {
                ContentSectionPayload.RichPayload coverContentPayload =
                        toCoverContentPayload(work.cover().getContent(), intThumbnailSpec);
                WorkProfileVM workProfile = new WorkProfileVM(work, coverContentPayload, author);


                workIdToWorkProfileVMMap.put(work.idStr(), workProfile);
            }
        }

        return workIdToWorkProfileVMMap;
    }

    public void select(String workId) {
        workRepo.setPresentPriorityOf(workId, Work.PresentPriority.selected);
    }

    public void cancelSelect(String workId) {
        workRepo.setPresentPriorityOf(workId, Work.PresentPriority.raw);
    }

    public void ordinary(String workId) {
        workRepo.setPresentPriorityOf(workId, Work.PresentPriority.ordinary);
    }

    public void cancelOrdinary(String workId) {
        workRepo.setPresentPriorityOf(workId, Work.PresentPriority.raw);
    }

    public void bad(String workId) {
        workRepo.setPresentPriorityOf(workId, Work.PresentPriority.bad);
    }

    public void cancelBad(String workId) {
        workRepo.setPresentPriorityOf(workId, Work.PresentPriority.raw);
    }

    public ContentSectionPayload.RichPayload toCoverContentPayload(
            Rich coverContent, Optional<Integer> intThumbnailSpec) {
        ContentSectionPayload.RichPayload targetDTO = new ContentSectionPayload.RichPayload();
        Beans.copyProperties(coverContent, targetDTO);
        targetDTO.fileUrl = storageService.imgDownloadUrl(coverContent.fileKey(), intThumbnailSpec);
        return targetDTO;
    }

}
