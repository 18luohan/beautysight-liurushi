/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.common.utils;

import org.slf4j.MDC;

/**
 * @author chenlong
 * @since 1.0
 */
public final class Logs {

    private static final String REQUEST_ID = "req.id";
    private static final String USER_ID = "user.id";

    public static void putReqId(String requestId) {
        MDC.put(REQUEST_ID, requestId);
        MDC.put(USER_ID, "visitor");
    }

    public static void putUserId(String userId) {
        MDC.put(USER_ID, userId);
        RequestLogContext.putUserId(userId);
    }

    public static void clearMDC() {
        removeReqId();
        removeUserId();
    }

    private static void removeReqId() {
        MDC.remove(REQUEST_ID);
    }

    private static void removeUserId() {
        MDC.remove(USER_ID);
    }

}
