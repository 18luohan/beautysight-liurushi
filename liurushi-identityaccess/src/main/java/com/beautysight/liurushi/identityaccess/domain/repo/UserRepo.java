/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.domain.repo;

import com.beautysight.liurushi.fundamental.infrastructure.persistence.mongo.MongoRepository;
import com.beautysight.liurushi.identityaccess.domain.model.User;
import com.google.common.base.Optional;

import java.util.List;

/**
 * @author chenlong
 * @since 1.0
 */
public interface UserRepo extends MongoRepository<User> {

    Optional<User> withMobile(String mobile);

    Optional<User> withGlobalId(String globalId);

    void updateLastLoginTime(User loggingInUser);

    void setUsersGroupToProfessional(List<String> mobiles);

}
