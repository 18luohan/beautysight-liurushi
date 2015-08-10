/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.infrastructure.persistence;

import com.beautysight.liurushi.community.domain.model.work.picstory.PictureStoryRepo;
import com.beautysight.liurushi.test.SpringBasedAppTest;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author chenlong
 * @since 1.0
 */
public class PictureStoryRepoImplTest extends SpringBasedAppTest {

    @Autowired
    private PictureStoryRepo pictureStoryRepo;

//    @Test
//    public void findPictureStoriesInRange() {
//        String referenceWorkId = "55a787c96c12e410a779e9fe";
//        int offset = 2;
//        List<PictureStory> pictureStories = pictureStoryRepo.findPictureStoriesInRange(referenceWorkId, offset);
//        Assert.assertEquals(5, pictureStories.size());
//    }
//
//    @Test
//    public void test() {
//        PictureStory story = pictureStoryRepo.findOne(new ObjectId("556d7fd0f8d0efa2c55361f1"));
//        System.out.println(story.id());
//    }

}