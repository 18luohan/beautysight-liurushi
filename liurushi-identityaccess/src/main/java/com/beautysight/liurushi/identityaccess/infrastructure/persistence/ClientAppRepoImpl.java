/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.infrastructure.persistence;

import com.beautysight.liurushi.fundamental.infrastructure.persistence.mongo.AbstractMongoRepository;
import com.beautysight.liurushi.identityaccess.domain.user.ClientApp;
import com.beautysight.liurushi.identityaccess.domain.user.ClientAppRepo;
import com.google.common.base.Optional;
import org.mongodb.morphia.query.Query;
import org.springframework.stereotype.Repository;

/**
 * @author chenlong
 * @since 1.0
 */
@Repository
public class ClientAppRepoImpl extends AbstractMongoRepository<ClientApp> implements ClientAppRepo {

    @Override
    public Optional<ClientApp> with(ClientApp.AppName name, Integer internalVersion) {
        Query<ClientApp> query = newQuery().field("name").equal(name)
                .field("internalVersion").equal(internalVersion);
        return Optional.fromNullable(query.get());
    }

    @Override
    public Optional<ClientApp> with(ClientApp.AppName name, ClientApp.Tag tag) {
        Query<ClientApp> query = newQuery().field("name").equal(name)
                .field("tag").equal(tag);
        return Optional.fromNullable(query.get());
    }

    @Override
    protected Class<ClientApp> entityClass() {
        return ClientApp.class;
    }
}
