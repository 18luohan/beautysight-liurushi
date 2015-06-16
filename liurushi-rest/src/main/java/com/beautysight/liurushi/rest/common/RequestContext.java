/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.rest.common;

import com.beautysight.liurushi.common.utils.Logs;
import com.beautysight.liurushi.identityaccess.domain.model.User;
import com.beautysight.liurushi.identityaccess.domain.model.UserClient;
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
public class RequestContext {

    private static final Logger logger = LoggerFactory.getLogger(RequestContext.class);

    private static final ThreadLocal<UserClient> thisUserClient = new ThreadLocal<>();

    public static void putThisUserClient(UserClient userClient) {
        thisUserClient.set(userClient);
        Logs.debug(logger, "Put userClient into request context: {}", userClient);
    }

    public static UserClient thisUserClient() {
        return thisUserClient.get();
    }

    public static boolean isThisUserAMember() {
        return (thisUserClient().userType() == User.Type.member);
    }

    public static void clear() {
        // TODO 是否真的有必要使用同步？
        synchronized (thisUserClient) {
            UserClient userClient = thisUserClient.get();
            thisUserClient.remove();
            Logs.debug(logger, "Cleared userClient from request context: {}", userClient);
        }
    }

}
