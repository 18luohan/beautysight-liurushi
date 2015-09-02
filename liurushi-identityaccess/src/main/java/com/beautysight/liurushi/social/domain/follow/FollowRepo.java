/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.social.domain.follow;

import com.beautysight.liurushi.common.domain.Pageable;
import com.beautysight.liurushi.fundamental.infrastructure.persistence.mongo.MongoRepository;
import com.google.common.base.Optional;

import java.util.List;

/**
 * @author chenlong
 * @since 1.0
 */
public interface FollowRepo extends MongoRepository<Follow> {

    Optional<Follow> getBy(String followerId, String followingId);

    int deleteBy(String followerId, String followingId);

    List<Follow> findFollowInRange(QueryType type, String involvedUserId, Pageable range);

    enum QueryType {
        follower, following
    }

}
