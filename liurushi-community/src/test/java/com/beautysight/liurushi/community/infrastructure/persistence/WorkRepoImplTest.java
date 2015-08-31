/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.infrastructure.persistence;

import com.beautysight.liurushi.community.domain.model.work.Work;
import com.beautysight.liurushi.community.domain.model.work.WorkRepo;
import com.beautysight.liurushi.test.SpringBasedAppTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author chenlong
 * @since 1.0
 */
public class WorkRepoImplTest extends SpringBasedAppTest {

    @Autowired
    private WorkRepo workRepo;

    @Test
    public void getPictureStoryOf() {
        Work work = workRepo.getPictureStoryOf("55e431175e76a9cdbb532ed1");
        Assert.assertNotNull(work.pictureStory().layout());
        Assert.assertNotNull(work.pictureStory().layout().generateBlockLocator());
    }

}