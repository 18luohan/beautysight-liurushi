/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.domain.repo;

import com.beautysight.liurushi.common.persistence.mongo.MongoRepository;
import com.beautysight.liurushi.identityaccess.domain.model.User;

import java.util.List;

/**
 * Here is Javadoc.
 * <p/>
 * Created by chenlong on 2015-05-13.
 *
 * @author chenlong
 * @since 1.0
 */
public interface UserRepository extends MongoRepository<User> {

    List<User> withMobilePhone(String mobilePhone);

}
