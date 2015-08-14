/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.rest.common;

import com.beautysight.liurushi.common.ex.ApplicationException;
import com.beautysight.liurushi.identityaccess.app.command.AccessTokenDPO;
import com.beautysight.liurushi.identityaccess.domain.model.AccessToken;
import com.beautysight.liurushi.identityaccess.domain.model.UserLite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author chenlong
 * @since 1.0
 */
public class RequestContext {

    private static final Logger logger = LoggerFactory.getLogger(RequestContext.class);

    private static final ThreadLocal<AccessTokenDPO> accessToken = new ThreadLocal<AccessTokenDPO>() {
        @Override
        protected AccessTokenDPO initialValue() {
            return null;
        }
    };

    private static final ThreadLocal<UserLite> user = new ThreadLocal<UserLite>() {
        @Override
        protected UserLite initialValue() {
            return null;
        }
    };

    public static void putAccessToken(AccessTokenDPO theToken) {
        accessToken.set(theToken);
        if (logger.isDebugEnabled()) {
            logger.debug("Put access token into request context: {}", accessToken);
        }
    }

    public static AccessTokenDPO getAccessToken() {
        if (accessToken.get() == null) {
            throw new ApplicationException("Expected to get access token from request context, but actual not present");
        }
        return accessToken.get();
    }

    public static void putUserProfile(UserLite theUser) {
        user.set(theUser);
        if (logger.isDebugEnabled()) {
            logger.debug("Put user profile into request context: {}", theUser);
        }
    }

    public static UserLite getUser() {
        if (user.get() == null) {
            throw new ApplicationException("Expected to get current user profile from request context, but actual not present");
        }
        return user.get();
    }

    public static boolean isThisUserAMember() {
        return (accessToken.get() != null
                && accessToken.get().type == AccessToken.Type.Bearer);
    }

    public static void clear() {
        // TODO 是否真的有必要使用同步？
        synchronized (accessToken) {
            AccessTokenDPO theToken = accessToken.get();
            accessToken.remove();
            if (logger.isDebugEnabled()) {
                logger.debug("Cleared access token from request context: {}", theToken);
            }

            UserLite theUser = user.get();
            user.remove();
            if (logger.isDebugEnabled()) {
                logger.debug("Cleared user profile from request context: {}", theUser);
            }
        }
    }

}
