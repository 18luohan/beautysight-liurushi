/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.fundamental.infrastructure.persistence;

import com.beautysight.liurushi.common.persistence.mongo.AbstractMongoRepository;
import com.beautysight.liurushi.fundamental.domain.appconfig.AppConfig;
import com.beautysight.liurushi.fundamental.domain.appconfig.AppConfigRepo;
import com.google.common.base.Optional;
import org.springframework.stereotype.Repository;

/**
 *
 * @author chenlong
 * @since 1.0
 */
@Repository
public class AppConfigRepoImpl extends AbstractMongoRepository<AppConfig> implements AppConfigRepo {

    @Override
    public Optional<AppConfig> withName(String configItemName) {
        return findOneBy(Conditions.of("name", configItemName));
    }

    @Override
    protected Class<AppConfig> entityClass() {
        return AppConfig.class;
    }

}