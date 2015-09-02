/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.social.infrastructure.persistence;

import com.beautysight.liurushi.common.domain.OffsetDirection;
import com.beautysight.liurushi.common.domain.Pageable;
import com.beautysight.liurushi.fundamental.infrastructure.persistence.mongo.AbstractMongoRepository;
import com.beautysight.liurushi.social.domain.follow.Follow;
import com.beautysight.liurushi.social.domain.follow.FollowRepo;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.mongodb.WriteResult;
import org.apache.commons.collections.CollectionUtils;
import org.bson.types.ObjectId;
import org.mongodb.morphia.query.Query;
import org.springframework.stereotype.Repository;

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
    public List<Follow> findFollowInRange(QueryType type, String involvedUserId, Pageable range) {
        Preconditions.checkArgument(range.offset() > 0, "Assert range.offset() > 0");

        if (!range.referencePoint().isPresent()) {
            return getLatestFollow(type, involvedUserId, range.offset());
        }

        Query<Follow> query;
        List<Follow> result = new ArrayList<>();
        if (range.direction() == OffsetDirection.both || range.direction() == OffsetDirection.after) {
            query = newQuery();
            query.field(determineField(type)).equal(new ObjectId(involvedUserId))
                    .field("id").greaterThanOrEq(new ObjectId(range.referencePoint().get()))
                    .order("id").limit(range.offset() + 1);
            List<Follow> ascendingList = query.asList();
            if (CollectionUtils.isNotEmpty(ascendingList)) {
                // 倒序排列
                Collections.reverse(ascendingList);
                result.addAll(ascendingList);
            }
        }

        if (range.direction() == OffsetDirection.both || range.direction() == OffsetDirection.before) {
            query = newQuery();
            query.field(determineField(type)).equal(new ObjectId(involvedUserId))
                    .field("id").lessThan(new ObjectId(range.referencePoint().get()))
                    // 目前morphia组件还不支持$natural查询修饰符
                    .order("-id").limit(range.offset());
            List<Follow> descendingList = query.asList();
            if (CollectionUtils.isNotEmpty(descendingList)) {
                result.addAll(descendingList);
            }
        }

        return result;

    }

    private List<Follow> getLatestFollow(QueryType type, String involvedUserId, int count) {
        Query<Follow> query = newQuery()
                .field(determineField(type)).equal(new ObjectId(involvedUserId))
                .order("-id").limit(count);
        return query.asList();
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
