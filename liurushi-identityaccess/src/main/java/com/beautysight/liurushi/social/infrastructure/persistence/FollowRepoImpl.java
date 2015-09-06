/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.social.infrastructure.persistence;

import com.beautysight.liurushi.common.domain.Range;
import com.beautysight.liurushi.fundamental.infrastructure.persistence.mongo.AbstractMongoRepository;
import com.beautysight.liurushi.social.domain.follow.Follow;
import com.beautysight.liurushi.social.domain.follow.FollowRepo;
import com.beautysight.liurushi.social.domain.follow.UserInFollow;
import com.google.common.base.Optional;
import org.bson.types.ObjectId;
import org.mongodb.morphia.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
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
    public List<UserInFollow> findUsersInFollowInRange(QueryType type, String involvedUserId, Range range) {
        Conditions conditions = Conditions.of(determineConditionField(type), toMongoId(involvedUserId));
        List<Follow> follows = find(conditions, range);
        if (CollectionUtils.isEmpty(follows)) {
            return Collections.EMPTY_LIST;
        }

        List<UserInFollow> result = new ArrayList<>(follows.size());
        for (Follow follow : follows) {
            String userId = determineFollowerOrFollowingId(type, follow);
            result.add(new UserInFollow(follow.idAsStr(), userId));
        }

        return result;
    }

    @Override
    public List<Follow> findFollowsBy(String followerId, List<String> followingIds) {
        return newQuery().field("followerId").equal(toMongoId(followerId))
                .field("followingId").in(toMongoIds(followingIds)).asList();
    }

    private String determineConditionField(QueryType type) {
        if (type == QueryType.follower) {
            return "followingId";
        } else if (type == QueryType.following) {
            return "followerId";
        } else {
            throw new RuntimeException("Unexpected QueryType: " + type);
        }
    }

    private String determineFollowerOrFollowingId(QueryType type, Follow follow) {
        if (type == QueryType.follower) {
            return follow.followerId();
        } else if (type == QueryType.following) {
            return follow.followingId();
        } else {
            throw new RuntimeException("Unexpected QueryType: " + type);
        }
    }

    @Override
    protected Class<Follow> entityClass() {
        return Follow.class;
    }

}
