/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.port.adapter.persistence;

import com.beautysight.liurushi.common.persistence.mongo.AbstractMongoRepository;
import com.beautysight.liurushi.identityaccess.domain.model.AccessToken;
import com.beautysight.liurushi.identityaccess.domain.model.Device;
import com.beautysight.liurushi.identityaccess.domain.model.User;
import com.beautysight.liurushi.identityaccess.domain.repo.AccessTokenRepository;
import com.google.common.base.Optional;
import org.mongodb.morphia.query.Query;
import org.springframework.stereotype.Repository;

/**
 * Here is Javadoc.
 * <p/>
 * Created by chenlong on 2015-05-14.
 *
 * @author chenlong
 * @since 1.0
 */
@Repository
public class AccessTokenRepositoryImpl extends AbstractMongoRepository<AccessToken> implements AccessTokenRepository {

    @Override
    public Optional<AccessToken> issuedFor(User user, Device device) {
        Query<AccessToken> query = newQuery(Conditions.of("userId", user.idAsString()).and("deviceId", device.idAsString()));
        return Optional.fromNullable(query.get());
    }

    @Override
    protected Class<AccessToken> entityClass() {
        return AccessToken.class;
    }

}
