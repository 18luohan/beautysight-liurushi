/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.rest.common;

import com.beautysight.liurushi.common.ex.ApplicationException;
import com.beautysight.liurushi.identityaccess.domain.auth.AccessToken;
import com.google.common.base.Optional;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author chenlong
 * @since 1.0
 */
public class RequestContext {

    private static final Logger logger = LoggerFactory.getLogger(RequestContext.class);

    private static final ThreadLocal<AccessToken> accessToken = new ThreadLocal<AccessToken>() {
        @Override
        protected AccessToken initialValue() {
            return null;
        }
    };

    public static void putAccessToken(AccessToken theToken) {
        accessToken.set(theToken);
        if (logger.isDebugEnabled()) {
            logger.debug("Put access token into request context: {}", theToken);
        }
    }

    public static AccessToken currentAccessToken() {
        if (accessToken.get() == null) {
            throw new ApplicationException("Access token absent in request context");
        }
        return accessToken.get();
    }

    public static String currentUserId() {
        String currentUserId = currentAccessToken().userId();
        if (StringUtils.isBlank(currentUserId)) {
            throw new ApplicationException("Current user id absent in request context");
        }
        return currentUserId;
    }

    public static Optional<String> optionalCurrentUserId() {
        if (accessToken.get() == null) {
            return Optional.absent();
        }
        return Optional.of(currentUserId());
    }

    public static boolean isThisUserAMember() {
        return (accessToken.get() != null
                && accessToken.get().type() == AccessToken.Type.Bearer);
    }

    public static void clear() {
        // TODO 是否真的有必要使用同步？
        synchronized (accessToken) {
            AccessToken theToken = accessToken.get();
            accessToken.remove();
            if (logger.isDebugEnabled()) {
                logger.debug("Cleared access token from request context: {}", theToken);
            }
        }
    }

}
