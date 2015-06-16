/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.fundamental.domain.appconfig;

import com.beautysight.liurushi.common.persistence.mongo.MongoRepository;
import com.google.common.base.Optional;

/**
 *
 * @author chenlong
 * @since 1.0
 */
public interface AppConfigRepo extends MongoRepository<AppConfig> {

    Optional<AppConfig> withName(String configItemName);

}
