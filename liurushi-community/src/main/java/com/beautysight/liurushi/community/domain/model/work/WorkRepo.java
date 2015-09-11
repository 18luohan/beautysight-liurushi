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

    Work getWorkProfile(String workId);

    List<Work> getLatestWorks(Work.Source source, int count);

    List<Work> findWorkProfilesInRange(Work.Source source, Range range);

    Work getWorkOnlyWithPictureStory(String workId);

    List<Work> findAuthorWorksIn(AuthorWorksRange range);

    void increaseLikeTimesBy(int increment, String workId);

}
