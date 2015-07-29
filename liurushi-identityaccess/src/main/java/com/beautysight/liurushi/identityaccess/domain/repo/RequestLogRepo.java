/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.domain.repo;

import com.beautysight.liurushi.fundamental.infrastructure.persistence.mongo.MongoRepository;
import com.beautysight.liurushi.identityaccess.domain.model.RequestLog;

/**
 * @author chenlong
 * @since 1.0
 */
public interface RequestLogRepo extends MongoRepository<RequestLog> {
}
