/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.domain.model;

import com.beautysight.liurushi.common.domain.AbstractEntity;
import org.mongodb.morphia.annotations.Entity;

import java.util.Date;

/**
 * @author chenlong
 * @since 1.0
 */
@Entity(value = "request_logs", noClassnameStored = true)
public class RequestLog extends AbstractEntity {

    private String uri;
    private String queryString;
    private String protocolVersion;
    private String reqId;
    private Date reqTime;
    private Integer costOfTime;
    private String userId;

}