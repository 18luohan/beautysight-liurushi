/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.domain.model.like;

import com.beautysight.liurushi.fundamental.infrastructure.persistence.mongo.MongoRepository;
import com.google.common.base.Optional;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author chenlong
 * @since 1.0
 */
@Repository
public interface LikeRepo extends MongoRepository<Like> {

    int deleteLikeBy(String workId, String userId);

    Optional<Like> getLikeBy(String workId, String userId);

    List<Like> findLikesBy(String userId, List<String> workIds);

}
