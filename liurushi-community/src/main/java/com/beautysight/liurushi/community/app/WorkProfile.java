/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.app;

import com.beautysight.liurushi.common.app.PresentationModel;
import com.beautysight.liurushi.community.domain.model.work.Author;
import com.beautysight.liurushi.community.domain.model.work.Work;

import java.util.Date;

/**
 * 作品概略
 *
 * @author chenlong
 * @since 1.0
 */
public class WorkProfile implements PresentationModel {

    private String id;
    private String title;
    private String coverPictureUrl;
    private Author author;
    private Date publishedAt;

    public static WorkProfile from(Work work, String coverPictureUrl, Author author) {
        WorkProfile workProfile = new WorkProfile();
        workProfile.id = work.id().toString();
        workProfile.title = work.title();
        workProfile.coverPictureUrl = coverPictureUrl;
        workProfile.author = author;
        workProfile.publishedAt = work.publishedAt();
        return workProfile;
    }

}