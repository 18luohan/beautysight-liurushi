/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.rest.common;

import com.beautysight.liurushi.common.ex.ApplicationException;
import com.beautysight.liurushi.common.ex.CommonErrorId;
import com.beautysight.liurushi.common.utils.Logs;
import com.beautysight.liurushi.common.utils.RequestLogContext;
import com.beautysight.liurushi.identityaccess.app.auth.AuthCommand;
import com.beautysight.liurushi.identityaccess.app.auth.OAuthApp;
import com.beautysight.liurushi.identityaccess.app.user.UserApp;
import com.beautysight.liurushi.identityaccess.domain.auth.AccessToken;
import com.beautysight.liurushi.identityaccess.domain.auth.AccessTokenPayload;
import com.google.common.base.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author chenlong
 * @since 1.0
 */
public class AccessTokenAuthenticator extends HandlerInterceptorAdapter {

    private static final Logger logger = LoggerFactory.getLogger(AccessTokenAuthenticator.class);

    @Autowired
    private OAuthApp authApp;
    @Autowired
    private UserApp userApp;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (logger.isInfoEnabled()) {
            logger.info("Beginning to authenticate request");
        }

        try {
            Optional<AccessTokenPayload> clientProvidedToken = Requests.getAccessToken(request);
            if (logger.isDebugEnabled()) {
                logger.debug("Authenticating access token: {}", clientProvidedToken.orNull());
            }

            if (clientProvidedToken.isPresent()) {
                clientProvidedToken.get().validate();

                // 如果是refresh bearer token api，就跳过对请求的认证；否则校验access token。
                AccessToken theToken;
                if (isRefreshBearerTokenAPI(request)) {
                    theToken = authApp.getLastTokenOrCurrentToken(clientProvidedToken.get().accessToken, clientProvidedToken.get().type);
                } else {
                    theToken = authApp.authenticate(new AuthCommand(clientProvidedToken.get().type, clientProvidedToken.get().accessToken));
                }

                RequestContext.putAccessToken(theToken);
                Logs.putUserAndDeviceId(theToken.userId(), theToken.deviceId());
                RequestLogContext.putUserAndDeviceId(theToken.userId(), theToken.deviceId());
            }
        } catch (ApplicationException ex) {
            logger.error("Error while authenticate", ex);
            Responses.setStatusAndWriteTo(response, ex.errorId(), ex.getMessage());
            return false;
        } catch (Exception ex) {
            logger.error("Error while authenticate", ex);
            Responses.setStatusAndWriteTo(response, CommonErrorId.internal_server_error, ex.getMessage());
            return false;
        } finally {
            if (logger.isInfoEnabled()) {
                logger.info("Finished authenticating request");
            }
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler, Exception ex) throws Exception {
        super.afterCompletion(request, response, handler, ex);
        RequestContext.clear();
    }

    private boolean isRefreshBearerTokenAPI(HttpServletRequest request) {
        return (HttpMethod.PUT == HttpMethod.valueOf(request.getMethod())
                && request.getRequestURI().endsWith("/oauth/bearer_token"));
    }

}
