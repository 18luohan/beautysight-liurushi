/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Here is Javadoc.
 * <p/>
 * Created by chenlong on 2015-05-11.
 *
 * @author chenlong
 * @since 1.0
 */
public class RequestKeyInfoHolder {

    private static final Logger logger = LoggerFactory.getLogger(RequestKeyInfoHolder.class);

    private static final ThreadLocal<String> currentRequestId = new ThreadLocal<String>();

    public static String getCurrentRequestId() {
        return currentRequestId.get();
    }

    public static void setCurrentRequestId(String requestId) {
        currentRequestId.set(requestId);
        Logs.debug(logger, "Hold requestId for current thread: {}", requestId);
    }

    public static void clear() {
        // TODO 是否真的有必要使用同步？
        synchronized (currentRequestId) {
            String requestId = currentRequestId.get();
            currentRequestId.remove();
            Logs.debug(logger, "Remove requestId from current thread: {}", requestId);
        }
    }

}
