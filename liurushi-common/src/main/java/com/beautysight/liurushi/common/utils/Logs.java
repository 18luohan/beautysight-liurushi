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
    private static final String DEVICE_ID = "device.id";

    public static void putReqId(String requestId) {
        MDC.put(REQUEST_ID, requestId);
        MDC.put(USER_ID, "visitor");
    }

    public static void putUserAndDeviceId(String userId, String deviceId) {
        MDC.put(USER_ID, userId);
        MDC.put(DEVICE_ID, deviceId);
    }

    public static void clearMDC() {
        removeReqId();
        removeUserAndDeviceId();
    }

    private static void removeReqId() {
        MDC.remove(REQUEST_ID);
    }

    private static void removeUserAndDeviceId() {
        MDC.remove(USER_ID);
        MDC.remove(DEVICE_ID);
    }

}
