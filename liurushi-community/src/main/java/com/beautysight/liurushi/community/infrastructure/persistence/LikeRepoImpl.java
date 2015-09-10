/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.infrastructure.persistence;

import com.beautysight.liurushi.community.domain.model.like.Like;
import com.beautysight.liurushi.community.domain.model.like.LikeRepo;
import com.beautysight.liurushi.fundamental.infrastructure.persistence.mongo.AbstractMongoRepository;
import com.google.common.base.Optional;
import com.mongodb.DuplicateKeyException;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author chenlong
 * @since 1.0
 */
@Repository
public class LikeRepoImpl extends AbstractMongoRepository<Like> implements LikeRepo {

    private static final Logger logger = LoggerFactory.getLogger(LikeRepoImpl.class);

    @Override
    public boolean findAndCreateIfNotExist(Like newLike) {
        try {
            Query<Like> query = newQuery()
                    .field("workId").equal(newLike.workMongoId())
                    .field("userId").equal(newLike.userMongoId());
            UpdateOperations<Like> updateOps = newUpdateOps()
                    .setOnInsert("workId", newLike.workMongoId())
                    .setOnInsert("userId", newLike.userMongoId())
                    .setOnInsert("createdAt", newLike.createdAt());
            boolean oldVersion = true, createIfMissing = true;
            Like oldLike = datastore.findAndModify(query, updateOps, oldVersion, createIfMissing);
            return (oldLike == null);
        } catch (DuplicateKeyException ex) {
            logger.error(String.format("Error on create new like, workId: %s, userId: %s",
                    newLike.workId(), newLike.userId()), ex);
            return false;
        }
    }

    @Override
    public int deleteLikeBy(String workId, String userId) {
        Query<Like> query = newQuery()
                .field("workId").equal(toMongoId(workId))
                .field("userId").equal(toMongoId(userId));
        return datastore.delete(query)
                .getN();
    }

    @Override
    public Optional<Like> getLikeBy(String workId, String userId) {
        Query<Like> query = newQuery()
                .field("workId").equal(toMongoId(workId))
                .field("userId").equal(toMongoId(userId));
        return findOneBy(query);
    }

    @Override
    public List<Like> findLikesBy(String userId, List<String> workIds) {
        return newQuery().field("userId").equal(toMongoId(userId))
                .field("workId").in(toMongoIds(workIds)).asList();
    }

    @Override
    protected Class<Like> entityClass() {
        return Like.class;
    }

}
