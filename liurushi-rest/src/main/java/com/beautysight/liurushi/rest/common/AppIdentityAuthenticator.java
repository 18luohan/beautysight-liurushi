/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.rest.common;

import com.beautysight.liurushi.common.ex.CommonErrorId;
import com.beautysight.liurushi.common.utils.Logs;
import com.beautysight.liurushi.identityaccess.common.AuthErrorId;
import com.google.common.base.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author chenlong
 * @since 1.0
 */
public class AppIdentityAuthenticator extends HandlerInterceptorAdapter {

    private static final Logger logger = LoggerFactory.getLogger(AppIdentityAuthenticator.class);

    private static final String APP_ID_HEADER = "X-BeautySight-App-ID";

    private String mobileAppId;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Optional<String> appId = Requests.getHeader(APP_ID_HEADER, request);

        if (logger.isDebugEnabled()) {
            Logs.debug(logger, "Request from app: {}, uri: {}",
                    appId.orNull(), Requests.methodAndURI(request));
        }

        if (!appId.isPresent()) {
            Responses.setStatusAndWriteTo(response, CommonErrorId.unauthorized,
                    String.format("%s header required", APP_ID_HEADER));
            return false;
        }

        if (!appId.get().equals(mobileAppId)) {
            Responses.setStatusAndWriteTo(response, AuthErrorId.invalid_app_id,
                    String.format("Invalid %s header value", APP_ID_HEADER));
            return false;
        }

        return true;
    }

    public void setMobileAppId(String mobileAppId) {
        this.mobileAppId = mobileAppId;
    }

}