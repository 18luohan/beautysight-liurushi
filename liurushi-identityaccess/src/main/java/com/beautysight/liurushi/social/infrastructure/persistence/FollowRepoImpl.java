/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.social.infrastructure.persistence;

import com.beautysight.liurushi.common.domain.Range;
import com.beautysight.liurushi.fundamental.infrastructure.persistence.mongo.AbstractMongoRepository;
import com.beautysight.liurushi.social.domain.follow.Follow;
import com.beautysight.liurushi.social.domain.follow.FollowRepo;
import com.google.common.base.Optional;
import org.bson.types.ObjectId;
import org.mongodb.morphia.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author chenlong
 * @since 1.0
 */
@Repository
public class FollowRepoImpl extends AbstractMongoRepository<Follow> implements FollowRepo {

    @Override
    public Optional<Follow> getBy(String followerId, String followingId) {
        Conditions conditions = Conditions.of("followerId", new ObjectId(followerId))
                .and("followingId", new ObjectId(followingId));
        return findOneBy(conditions);
    }

    @Override
    public int deleteBy(String followerId, String followingId) {
        Query<Follow> query = newQuery(
                Conditions.of("followerId", new ObjectId(followerId))
                        .and("followingId", new ObjectId(followingId)));
        return datastore.delete(query).getN();
    }

    @Override
    public List<Follow> findFollowInRange(QueryType type, String involvedUserId, Range range) {
        Conditions conditions = Conditions.of(determineField(type), toMongoId(involvedUserId));
        return find(conditions, range);
    }

    private String determineField(QueryType type) {
        if (type == QueryType.follower) {
            return "followingId";
        } else if (type == QueryType.following) {
            return "followerId";
        } else {
            throw new RuntimeException("Unexpected FollowType: " + type);
        }
    }

    @Override
    protected Class<Follow> entityClass() {
        return Follow.class;
    }

}
