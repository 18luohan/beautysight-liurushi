/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.infrastructure.persistence;

import com.beautysight.liurushi.common.domain.CountResult;
import com.beautysight.liurushi.community.domain.work.Work;
import com.beautysight.liurushi.community.domain.work.WorkRepo;
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
        Work work = workRepo.getWorkOnlyWithPictureStory("55e431175e76a9cdbb532ed1");
        Assert.assertNotNull(work.story().layout());
        Assert.assertNotNull(work.story().layout().generateBlockLocator());
    }

    @Test
    public void countWorksByPriority() {
        CountResult countResult = workRepo.countWorksByPresentPriority(Work.PresentPriority.raw);
        System.out.println(countResult.getCount());
    }

}