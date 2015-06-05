/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.infrastructure.persistence;

import com.beautysight.liurushi.community.domain.model.content.PictureStory;
import com.beautysight.liurushi.community.domain.model.content.PictureStoryRepo;
import com.beautysight.liurushi.test.SpringBasedAppTest;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

/**
 * Here is Javadoc.
 * <p/>
 * Created by chenlong on 2015-06-01.
 *
 * @author chenlong
 * @since 1.0
 */
public class PictureStoryRepoImplTest extends SpringBasedAppTest {

    @Autowired
    private PictureStoryRepo pictureStoryRepo;

    @Test
    public void test() {
        PictureStory story = pictureStoryRepo.findOne(new ObjectId("556d7fd0f8d0efa2c55361f1"));
        System.out.println(story.id());
    }

}