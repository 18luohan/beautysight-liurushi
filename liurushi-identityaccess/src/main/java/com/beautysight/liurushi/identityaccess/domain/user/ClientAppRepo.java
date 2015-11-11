/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.domain.user;

import com.beautysight.liurushi.fundamental.infrastructure.persistence.mongo.MongoRepository;
import com.google.common.base.Optional;

/**
 * @author chenlong
 * @since 1.0
 */
public interface ClientAppRepo extends MongoRepository<ClientApp> {

    Optional<ClientApp> with(ClientApp.AppName name, Integer internalVersion);

    Optional<ClientApp> with(ClientApp.AppName name, ClientApp.Tag tag);

}
