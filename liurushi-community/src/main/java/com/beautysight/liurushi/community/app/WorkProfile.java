/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.app;

import com.beautysight.liurushi.common.app.PresentationModel;
import com.beautysight.liurushi.common.utils.Beans;
import com.beautysight.liurushi.community.domain.model.content.Author;
import com.beautysight.liurushi.community.domain.model.content.PictureStory;

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
    private Date publishedAt;
    private Author author;

    public static WorkProfile from(PictureStory pictureStory, String coverPictureUrl, Author author) {
        WorkProfile workProfile = new WorkProfile();
        Beans.copyProperties(pictureStory, workProfile);
        workProfile.id = pictureStory.id().toString();
        workProfile.coverPictureUrl = coverPictureUrl;
        workProfile.author = author;
        return workProfile;
    }

}