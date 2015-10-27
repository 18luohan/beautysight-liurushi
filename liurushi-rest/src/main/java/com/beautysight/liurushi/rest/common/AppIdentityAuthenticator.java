/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.rest.common;

import com.beautysight.liurushi.common.ex.CommonErrorId;
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

    private String mobileAppId;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Optional<String> appId = Requests.appIdOf(request);

        if (logger.isInfoEnabled()) {
            logger.info("Authenticating app identity from which this request was sent: {}", appId.orNull());
        }

        if (!appId.isPresent()) {
            Responses.setStatusAndWriteTo(response, CommonErrorId.bad_request,
                    String.format("%s header required", Requests.APP_ID_HEADER));
            return false;
        }

        if (!appId.get().equals(mobileAppId)) {
            Responses.setStatusAndWriteTo(response, AuthErrorId.illegal_app_client,
                    String.format("Illegal %s header value", Requests.APP_ID_HEADER));
            return false;
        }

        return true;
    }

    public void setMobileAppId(String mobileAppId) {
        this.mobileAppId = mobileAppId;
    }

}