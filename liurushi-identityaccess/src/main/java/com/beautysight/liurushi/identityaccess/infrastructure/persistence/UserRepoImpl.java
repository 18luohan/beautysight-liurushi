/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.infrastructure.persistence;

import com.beautysight.liurushi.fundamental.infrastructure.persistence.mongo.AbstractMongoRepository;
import com.beautysight.liurushi.identityaccess.domain.model.User;
import com.beautysight.liurushi.identityaccess.domain.repo.UserRepo;
import com.google.common.base.Optional;
import org.bson.types.ObjectId;
import org.mongodb.morphia.query.UpdateOperations;
import org.springframework.stereotype.Repository;

/**
 * @author chenlong
 * @since 1.0
 */
@Repository
public class UserRepoImpl extends AbstractMongoRepository<User> implements UserRepo {

    @Override
    public Optional<User> withMobile(String mobile) {
        return findOneBy(Conditions.of("mobile", mobile));
    }

    @Override
    public void updateLastLoginTime(User loggingInUser) {
        UpdateOperations<User> updateOps = newUpdateOps().set("lastLogin", loggingInUser.lastLogin());
        datastore.update(loggingInUser, updateOps);
    }

    @Override
    public User addAvatarFor(ObjectId userId, User.Avatar newAvatar) {
        UpdateOperations<User> updateOps = newUpdateOps().add("avatars", newAvatar, true);
        return datastore.findAndModify(newQuery(Conditions.of("id", userId)), updateOps);
    }

    @Override
    protected Class<User> entityClass() {
        return User.class;
    }

}
