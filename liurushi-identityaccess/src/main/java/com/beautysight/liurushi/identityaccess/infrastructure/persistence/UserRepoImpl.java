/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.infrastructure.persistence;

import com.beautysight.liurushi.fundamental.infrastructure.persistence.mongo.AbstractMongoRepository;
import com.beautysight.liurushi.identityaccess.domain.model.User;
import com.beautysight.liurushi.identityaccess.domain.repo.UserRepo;
import com.google.common.base.Optional;
import org.bson.types.ObjectId;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

/**
 * @author chenlong
 * @since 1.0
 */
@Repository
public class UserRepoImpl extends AbstractMongoRepository<User> implements UserRepo {

    private static final String[] liteFields = Fields.newInstance().append("nickname", "group", "maxAvatar").toArray();
    private static final String[] liteWithStatsFields = Fields.newInstance().append(liteFields).append("stats").toArray();
    private static final String[] commonExcludedFields = Fields.newInstance().append("password", "lastLogin", "originalAvatar").toArray();

    @Override
    public User getUser(String id) {
        Query<User> query = newQuery().retrievedFields(false, commonExcludedFields)
                .field("id").equal(new ObjectId(id));
        return query.get();
    }

    @Override
    public User getLiteUserBy(String id) {
        Query<User> query = newQuery().retrievedFields(true, liteFields)
                .field("id").equal(new ObjectId(id));
        return query.get();
    }

    @Override
    public List<User> getLiteUsersWithStats(List<String> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.emptyList();
        }

        Query<User> query = newQuery().retrievedFields(true, liteWithStatsFields)
                .field("id").in(toMongoIds(ids));
        return query.asList();
    }

    @Override
    public Optional<User> withMobile(String mobile) {
        return findOneBy(Conditions.of("mobile", mobile));
    }

    @Override
    public Optional<User> withGlobalId(String globalId) {
        return findOneBy(Conditions.of("globalId", globalId));
    }

    @Override
    public void updateLastLoginTime(User loggingInUser) {
        UpdateOperations<User> updateOps = newUpdateOps().set("lastLogin", loggingInUser.lastLogin());
        datastore.update(loggingInUser, updateOps);
    }

    @Override
    public void setUsersGroupToProfessional(List<String> mobiles) {
        Query query = newQuery().field("mobile").in(mobiles);
        UpdateOperations<User> updateOps = newUpdateOps().set("group", User.Group.professional);
        datastore.update(query, updateOps);
    }

    @Override
    public void increaseFollowersNumBy(int increment, String followingId) {
        Query<User> query = newQuery().field("id").equal(toMongoId(followingId));
        UpdateOperations<User> updateOps = newUpdateOps().inc("stats.followersNum", increment);
        datastore.update(query, updateOps);
    }

    @Override
    public void increaseFollowingsNumBy(int increment, String followerId) {
        Query<User> query = newQuery().field("id").equal(toMongoId(followerId));
        UpdateOperations<User> updateOps = newUpdateOps().inc("stats.followingsNum", increment);
        datastore.update(query, updateOps);
    }

    @Override
    protected Class<User> entityClass() {
        return User.class;
    }

}
