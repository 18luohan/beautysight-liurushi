/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.infrastructure.persistence;

import com.beautysight.liurushi.community.domain.model.like.Like;
import com.beautysight.liurushi.community.domain.model.like.LikeRepo;
import com.beautysight.liurushi.test.SpringBasedAppTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author chenlong
 * @since 1.0
 */
public class LikeRepoImplTest extends SpringBasedAppTest {

    @Autowired
    private LikeRepo likeRepo;

    @Test
    public void save() {
        Like newLike = new Like("55ee547983d602802e1a87a5", "55e69e8aca3a749ffdc29202");
        likeRepo.save(newLike);
    }

    @Test
    public void testFindAndCreateIfNotExist() throws Exception {
        Like newLike = new Like("55ee547983d602802e1a87a5", "55e69e8aca3a749ffdc29202");
        boolean successful = likeRepo.findAndCreateIfNotExist(newLike);
        System.out.println("\n\n\n>>>>>>>>>>>>>>>:" + successful);
    }

}