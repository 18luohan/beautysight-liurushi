package com.beautysight.liurushi.identityaccess.infrastructure.persistence;

/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

import com.beautysight.liurushi.identityaccess.domain.model.RequestLog;
import com.beautysight.liurushi.identityaccess.domain.repo.RequestLogRepo;
import com.beautysight.liurushi.test.SpringBasedAppTest;
import com.beautysight.liurushi.test.utils.Reflections;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

/**
 * @author chenlong
 * @since 1.0
 */
public class RequestLogRepoImplTest extends SpringBasedAppTest {

    @Autowired
    private RequestLogRepo requestLogRepo;

    @Test
    public void getBy() {
        RequestLog requestLog = requestLogRepo.getBy("UUID-001");
        Reflections.setField(requestLog, "costOfTime", new Long(30000));
        requestLogRepo.merge(requestLog);
    }

}