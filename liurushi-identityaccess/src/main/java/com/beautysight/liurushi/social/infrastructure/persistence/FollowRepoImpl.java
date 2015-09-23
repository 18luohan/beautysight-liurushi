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
import com.mongodb.DuplicateKeyException;
import org.bson.types.ObjectId;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(FollowRepoImpl.class);

    @Override
    public boolean findAndCreateIfNotExist(Follow newFollow) {
        try {
            Query<Follow> query = newQuery()
                    .field("followerId").equal(newFollow.followerMongoId())
                    .field("followingId").equal(newFollow.followingMongoId());
            UpdateOperations<Follow> updateOps = newUpdateOps()
                    .setOnInsert("followerId", newFollow.followerMongoId())
                    .setOnInsert("followingId", newFollow.followingMongoId())
                    .setOnInsert("createdAt", newFollow.createdAt());
            boolean oldVersion = true, createIfMissing = true;
            Follow oldLike = datastore.findAndModify(query, updateOps, oldVersion, createIfMissing);
            return (oldLike == null);
        } catch (DuplicateKeyException ex) {
            logger.error(String.format("Error on create new follow, followerId: %s, followingId: %s",
                    newFollow.followerId(), newFollow.followingId()), ex);
            return false;
        }
    }

    @Override
    public Optional<Follow> getBy(String followerId, String followingId) {
        Conditions conditions = Conditions.newWithEqual("followerId", new ObjectId(followerId))
                .andEqual("followingId", new ObjectId(followingId));
        return findOneBy(conditions);
    }

    @Override
    public int deleteBy(String followerId, String followingId) {
        Query<Follow> query = newQuery(
                Conditions.newWithEqual("followerId", toMongoId(followerId))
                        .andEqual("followingId", toMongoId(followingId)));
        return datastore.delete(query).getN();
    }

    @Override
    public List<UserInFollow> findUsersInFollowInRange(QueryType type, String involvedUserId, Range range) {
        Conditions conditions = Conditions.newWithEqual(determineConditionField(type), toMongoId(involvedUserId));
        List<Follow> follows = find(Optional.of(conditions), range);
        if (CollectionUtils.isEmpty(follows)) {
            return Collections.EMPTY_LIST;
        }

        List<UserInFollow> result = new ArrayList<>(follows.size());
        for (Follow follow : follows) {
            String userId = determineFollowerOrFollowingId(type, follow);
            result.add(new UserInFollow(follow.idStr(), userId));
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
