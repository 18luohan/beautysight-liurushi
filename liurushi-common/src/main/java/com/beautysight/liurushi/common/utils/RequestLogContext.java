/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * @author chenlong
 * @since 1.0
 */
public final class RequestLogContext {

    private static final Logger logger = LoggerFactory.getLogger(RequestLogContext.class);

    private static final ThreadLocal<RequestLogContext> reqLogCtxForCurrentReq = new ThreadLocal<RequestLogContext>() {
        @Override
        protected RequestLogContext initialValue() {
            if (logger.isInfoEnabled()) {
                logger.info("Init request log context for current request");
            }
            return new RequestLogContext();
        }
    };

    public static void clear() {
        reqLogCtxForCurrentReq.remove();
        if (logger.isInfoEnabled()) {
            logger.info("Clear request log context for current request");
        }
    }

    public static void putHttpInfo(String methodAndUri, String queryString, String protocolVersion) {
        reqLogCtxForCurrentReq().uri = methodAndUri;
        reqLogCtxForCurrentReq().queryString = queryString;
        reqLogCtxForCurrentReq().protocolVersion = protocolVersion;
    }

    public static void putUserAndDeviceId(String userId, String deviceId) {
        reqLogCtxForCurrentReq().userId = userId;
        reqLogCtxForCurrentReq().deviceId = deviceId;
    }

    public static void putReqId(String reqId) {
        reqLogCtxForCurrentReq().reqId = reqId;
        putRequestTime(new Date());
    }

    public static void setCostOfTimeUntilNow() {
        reqLogCtxForCurrentReq().costOfTime =
                (int) (System.currentTimeMillis() - reqLogCtxForCurrentReq().reqTime.getTime());
    }

//    public static String reqId() {
//        return reqLogCtxForCurrentReq().reqId;
//    }

    public static RequestLogContext copyReqLogCtxForCurrentReq() {
        RequestLogContext source = reqLogCtxForCurrentReq();
        RequestLogContext copy = new RequestLogContext();
        copy.uri = source.uri;
        copy.queryString = source.queryString;
        copy.protocolVersion = source.protocolVersion;
        copy.reqId = source.reqId;
        copy.reqTime = source.reqTime;
        copy.costOfTime = source.costOfTime;
        copy.userId = source.userId;
        copy.deviceId = source.deviceId;
        return copy;
    }

    private static void putRequestTime(Date requestTime) {
        reqLogCtxForCurrentReq().reqTime = requestTime;
    }

    private static RequestLogContext reqLogCtxForCurrentReq() {
        return reqLogCtxForCurrentReq.get();
    }

    private String uri;
    private String queryString;
    private String protocolVersion;
    private String reqId;
    private Date reqTime;
    private Integer costOfTime;
    private String userId = "visitor";
    private String deviceId;

//    public String reqId() {
//        return this.reqId;
//    }

}