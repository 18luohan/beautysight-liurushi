/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.port.adapter.persistence;

import com.beautysight.liurushi.common.persistence.mongo.AbstractMongoRepository;
import com.beautysight.liurushi.identityaccess.domain.model.User;
import com.beautysight.liurushi.identityaccess.domain.repo.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Here is Javadoc.
 * <p/>
 * Created by chenlong on 2015-05-13.
 *
 * @author chenlong
 * @since 1.0
 */
@Repository
public class UserRepositoryImpl extends AbstractMongoRepository<User> implements UserRepository {

    @Override
    public List<User> withMobilePhone(String mobilePhone) {
        return findBy(Conditions.of("mobilePhone", mobilePhone));
    }

    @Override
    protected Class<User> entityClass() {
        return User.class;
    }

}
