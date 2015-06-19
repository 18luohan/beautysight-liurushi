/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.domain.repo;

import com.beautysight.liurushi.fundamental.infrastructure.persistence.mongo.MongoRepository;
import com.beautysight.liurushi.identityaccess.domain.model.User;
import com.google.common.base.Optional;
import org.bson.types.ObjectId;

/**
 * @author chenlong
 * @since 1.0
 */
public interface UserRepo extends MongoRepository<User> {

    Optional<User> withMobile(String mobile);

    void updateLastLoginTime(User loggingInUser);

    User addAvatarFor(ObjectId userId, User.Avatar newAvatar);

}
