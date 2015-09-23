/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.infrastructure.persistence;

import com.beautysight.liurushi.community.domain.work.cs.ContentSection;
import com.beautysight.liurushi.community.domain.work.cs.ContentSectionRepo;
import com.beautysight.liurushi.test.SpringBasedAppTest;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Here is Javadoc.
 * <p/>
 * Created by chenlong on 2015-06-02.
 *
 * @author chenlong
 * @since 1.0
 */
public class ContentSectionRepoImplTest extends SpringBasedAppTest {

    @Autowired
    private ContentSectionRepo contentSectionRepo;

    @Test
    public void find() {
        ContentSection section = contentSectionRepo.findOne(new ObjectId("556c50655b665d9d8d866037"));
        System.out.println(section.id());
        System.out.println(section.getClass());
    }

}