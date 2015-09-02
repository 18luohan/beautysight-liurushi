/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.domain.model.work;

import com.beautysight.liurushi.common.domain.OffsetDirection;
import com.beautysight.liurushi.fundamental.infrastructure.persistence.mongo.MongoRepository;
import com.google.common.base.Optional;

import java.util.List;

/**
 * @author chenlong
 * @since 1.0
 */
public interface WorkRepo extends MongoRepository<Work> {

    List<Work> getLatestWorks(Work.Source source, int count);

    List<Work> findWorksInRange(
            Work.Source source, Optional<String> referenceWorkId, int offset, OffsetDirection direction);

    Work getPictureStoryOf(String workId);

}
