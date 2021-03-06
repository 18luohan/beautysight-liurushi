/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.domain.user;

import com.beautysight.liurushi.fundamental.infrastructure.persistence.mongo.MongoRepository;
import com.google.common.base.Optional;

import java.util.Collection;
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

    User getLiteUserBy(String id);

    List<User> getLiteUsersBy(Collection<String> ids);

    List<User> getLiteUsersWithStats(List<String> ids);

    User getUserWithoutPwd(String id);

    void increaseFollowersNumBy(int increment, String followingId);

    void increaseFollowingsNumBy(int increment, String followerId);

    void increaseWorksNumBy(int increment, String userId);

    void increaseFavoritesNumBy(int increment, String userId);

}
