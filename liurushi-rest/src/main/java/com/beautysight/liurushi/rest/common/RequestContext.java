/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.rest.common;

import com.beautysight.liurushi.common.ex.ApplicationException;
import com.beautysight.liurushi.common.utils.Logs;
import com.beautysight.liurushi.interfaces.identityaccess.facade.dto.AccessTokenDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author chenlong
 * @since 1.0
 */
public class RequestContext {

    private static final Logger logger = LoggerFactory.getLogger(RequestContext.class);

    private static final ThreadLocal<AccessTokenDTO> accessToken = new ThreadLocal<AccessTokenDTO>() {
        @Override
        protected AccessTokenDTO initialValue() {
            return null;
        }
    };

    public static void putAccessToken(AccessTokenDTO theToken) {
        accessToken.set(theToken);
        Logs.debug(logger, "Put access token into request context: {}", accessToken);
    }

    public static AccessTokenDTO getAccessToken() {
        if (accessToken.get() == null) {
            throw new ApplicationException("Expected to get access token from request context, but actual not present");
        }
        return accessToken.get();
    }

    public static boolean isThisUserAMember() {
        return (accessToken.get() != null
                && accessToken.get().type == AccessTokenDTO.Type.Bearer);
    }

    public static void clear() {
        // TODO 是否真的有必要使用同步？
        synchronized (accessToken) {
            AccessTokenDTO theToken = accessToken.get();
            accessToken.remove();
            Logs.debug(logger, "Cleared access token from request context: {}", theToken);
        }
    }

}
