/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.infrastructure.persistence;

import com.beautysight.liurushi.common.persistence.mongo.AbstractMongoRepository;
import com.beautysight.liurushi.identityaccess.domain.model.User;
import com.beautysight.liurushi.identityaccess.domain.repo.UserRepo;
import com.google.common.base.Optional;
import org.mongodb.morphia.query.UpdateOperations;
import org.springframework.stereotype.Repository;

/**
 * Here is Javadoc.
 * <p/>
 * Created by chenlong on 2015-05-13.
 *
 * @author chenlong
 * @since 1.0
 */
@Repository
public class UserRepoImpl extends AbstractMongoRepository<User> implements UserRepo {

    @Override
    public Optional<User> withMobilePhone(String mobilePhone) {
        return findOneBy(Conditions.of("mobilePhone", mobilePhone));
    }

    @Override
    public void updateLastLogin(User loggingInUser) {
        UpdateOperations<User> updateOps = newUpdateOps().set("lastLogin", loggingInUser.lastLogin());
        datastore.update(loggingInUser, updateOps);
    }

    @Override
    protected Class<User> entityClass() {
        return User.class;
    }

}
