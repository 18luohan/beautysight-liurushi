/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.infrastructure.persistence;

import com.beautysight.liurushi.common.ex.EntityNotFoundException;
import com.beautysight.liurushi.fundamental.infrastructure.persistence.mongo.AbstractMongoRepository;
import com.beautysight.liurushi.identityaccess.domain.model.RequestLog;
import com.beautysight.liurushi.identityaccess.domain.repo.RequestLogRepo;
import com.google.common.base.Optional;
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

    @Override
    public RequestLog getBy(String reqId) {
        Optional<RequestLog> requestLog = findOneBy(Conditions.of("reqId", reqId));
        if (!requestLog.isPresent()) {
            throw new EntityNotFoundException("RequestLog absent, reqId:%s", reqId);
        }
        return requestLog.get();
    }

}