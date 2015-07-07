/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.domain.repo;

import com.beautysight.liurushi.fundamental.infrastructure.persistence.mongo.MongoRepository;
import com.beautysight.liurushi.identityaccess.domain.model.AccessToken;
import com.beautysight.liurushi.identityaccess.domain.model.Device;
import com.google.common.base.Optional;
import org.bson.types.ObjectId;

/**
 * @author chenlong
 * @since 1.0
 */
public interface AccessTokenRepo extends MongoRepository<AccessToken> {

    Optional<AccessToken> basicTokenIssuedFor(Device device);

    Optional<AccessToken> bearerTokenIssuedFor(ObjectId userId, ObjectId deviceId);

    Optional<AccessToken> accessTokenOf(String token, AccessToken.Type type);

}
