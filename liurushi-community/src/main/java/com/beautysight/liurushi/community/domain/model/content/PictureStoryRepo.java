/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.domain.model.content;

import com.beautysight.liurushi.fundamental.infrastructure.persistence.mongo.MongoRepository;
import com.google.common.base.Optional;

import java.util.List;

/**
 * @author chenlong
 * @since 1.0
 */
public interface PictureStoryRepo extends MongoRepository<PictureStory> {

    List<PictureStory> getLatestPictureStories(Work.Source source, int count);

    List<PictureStory> findPictureStoriesInRange(
            Work.Source source, Optional<String> referenceWorkId, int offset, OffsetDirection direction);

}