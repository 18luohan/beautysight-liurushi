/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.domain.work.draft;

import com.beautysight.liurushi.fundamental.infrastructure.persistence.mongo.MongoRepository;

/**
 * @author chenlong
 * @since 1.0
 */
public interface PublishingWorkRepo extends MongoRepository<PublishingWork> {

    PublishingWork getAllFilesIn(String workId);

}