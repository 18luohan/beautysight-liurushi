/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.domain.model.work;

import com.beautysight.liurushi.common.domain.Range;
import com.beautysight.liurushi.community.app.command.AuthorWorksRange;
import com.beautysight.liurushi.fundamental.infrastructure.persistence.mongo.MongoRepository;

import java.util.List;

/**
 * @author chenlong
 * @since 1.0
 */
public interface WorkRepo extends MongoRepository<Work> {

    List<Work> getLatestWorks(Work.Source source, int count);

    List<Work> findWorksInRange(Work.Source source, Range range);

    Work getPictureStoryOf(String workId);

    List<Work> findAuthorWorksIn(AuthorWorksRange range);

}
