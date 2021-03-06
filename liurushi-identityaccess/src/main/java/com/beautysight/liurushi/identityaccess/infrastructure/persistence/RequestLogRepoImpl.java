/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.infrastructure.persistence;

import com.beautysight.liurushi.fundamental.infrastructure.persistence.mongo.AbstractMongoRepository;
import com.beautysight.liurushi.identityaccess.domain.requestlog.RequestLog;
import com.beautysight.liurushi.identityaccess.domain.requestlog.RequestLogRepo;
import org.springframework.stereotype.Repository;

/**
 * @author chenlong
 * @since 1.0
 */
@Repository("requestLogRepo")
public class RequestLogRepoImpl extends AbstractMongoRepository<RequestLog> implements RequestLogRepo {

    @Override
    protected Class<RequestLog> entityClass() {
        return RequestLog.class;
    }

}