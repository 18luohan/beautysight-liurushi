/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.domain.model.content;

import com.beautysight.liurushi.fundamental.infrastructure.persistence.mongo.MongoRepository;

import java.util.List;

/**
 * @author chenlong
 * @since 1.0
 */
public interface PictureStoryRepo extends MongoRepository<PictureStory> {

    List<PictureStory> getLatestPictureStories(int count);
    List<PictureStory> findPictureStoriesInRange(String referenceWorkId, int offset);

}