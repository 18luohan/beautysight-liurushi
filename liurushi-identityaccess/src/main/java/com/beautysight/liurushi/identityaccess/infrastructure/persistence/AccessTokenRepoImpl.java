/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.infrastructure.persistence;

import com.beautysight.liurushi.fundamental.infrastructure.persistence.mongo.AbstractMongoRepository;
import com.beautysight.liurushi.identityaccess.domain.auth.AccessToken;
import com.beautysight.liurushi.identityaccess.domain.user.Device;
import com.beautysight.liurushi.identityaccess.domain.auth.AccessTokenRepo;
import com.google.common.base.Optional;
import org.bson.types.ObjectId;
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
public class AccessTokenRepoImpl extends AbstractMongoRepository<AccessToken> implements AccessTokenRepo {

    @Override
    public Optional<AccessToken> bearerTokenIssuedFor(ObjectId userId, ObjectId deviceId) {
        return findOneBy(Conditions.newInstance()
                .andEqual("type", AccessToken.Type.Bearer.toString())
                .andEqual("userId", userId)
                .andEqual("deviceId", deviceId));
    }

    @Override
    public Optional<AccessToken> basicTokenIssuedFor(Device device) {
        return findOneBy(Conditions.newInstance().andEqual("type", AccessToken.Type.Basic.toString())
                .andEqual("deviceId", device.id()));
    }

    @Override
    public Optional<AccessToken> accessTokenOf(String token, AccessToken.Type type) {
        return findOneBy(Conditions.newInstance().andEqual("type", type)
                .andEqual("accessToken", token));
    }

    @Override
    public Optional<AccessToken> lastAccessTokenOf(String token, AccessToken.Type type) {
        return findOneBy(Conditions.newInstance().andEqual("type", type)
                .andEqual("lastAccessToken", token));
    }

    @Override
    protected Class<AccessToken> entityClass() {
        return AccessToken.class;
    }

}
