/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.domain.requestlog;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.util.Date;

/**
 * @author chenlong
 * @since 1.0
 */
@Entity(value = "request_logs", noClassnameStored = true)
public class RequestLog {

    @Id
    protected ObjectId id;
    private String uri;
    private String queryString;
    private String protocolVersion;
    private String reqId;
    private Date reqTime;
    private Integer costOfTime;
    private String userId;
    private String deviceId;

}