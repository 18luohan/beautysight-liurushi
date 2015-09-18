/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.domain.model.work;

import com.beautysight.liurushi.common.domain.Range;
import com.beautysight.liurushi.fundamental.infrastructure.persistence.mongo.MongoRepository;
import com.google.common.base.Optional;

import java.util.List;

/**
 * @author chenlong
 * @since 1.0
 */
public interface DiscardedWorkRepo extends MongoRepository<DiscardedWork> {

    Optional<DiscardedWork> get(String workId);

    DiscardedWork getFullWork(String workId);

    List<DiscardedWork> findInRange(Range range);

}
