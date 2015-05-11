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
public class Logs {

    public static String prefixRequestIdTo(String log) {
        return String.format("[%s] %s", RequestKeyInfoHolder.getCurrentRequestId(), log);
    }

    public static Logger getLogger(Class<?> clazz) {
        return LoggerFactory.getLogger(clazz);
    }

    /**
     * Log the given message at the DEBUG level according to the specified format
     * and arguments. And this method prefixes the current request id to the resulted message.
     * @param logger
     * @param format
     * @param args
     */
    public static void debug(Logger logger, String format, Object... args) {
        debug(logger, true, format, args);
    }

    /**
     * Log the given message at the DEBUG level according to the specified format
     * and arguments. But this method don't prefix the current request id to the resulted message.
     * @param logger
     * @param format
     * @param args
     */
    public static void debugWithoutPrefixRequestId(Logger logger, String format, Object... args) {
        debug(logger, false, format, args);
    }

    private static void debug(Logger logger, boolean needPrefixRequestId, String format, Object... args) {
        if (!logger.isDebugEnabled()) {
            return;
        }

        if (needPrefixRequestId) {
            logger.debug(prefixRequestIdTo(format), args);
        } else {
            logger.debug(format, args);
        }
    }
}
