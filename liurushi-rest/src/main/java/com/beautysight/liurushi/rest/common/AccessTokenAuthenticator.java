/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.rest.common;

import com.beautysight.liurushi.common.ex.ApplicationException;
import com.beautysight.liurushi.common.ex.CommonErrorId;
import com.beautysight.liurushi.identityaccess.app.OAuthApp;
import com.beautysight.liurushi.identityaccess.app.command.AuthCommand;
import com.beautysight.liurushi.interfaces.identityaccess.facade.dto.AccessTokenDTO;
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

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (logger.isInfoEnabled()) {
            logger.info("Beginning to authenticate access token");
        }

        try {
            Optional<AccessTokenDTO> accessToken = Requests.getAccessToken(request);
            if (logger.isDebugEnabled()) {
                logger.debug("Authenticating access token: {}", accessToken.orNull());
            }

            if (accessToken.isPresent()) {
                accessToken.get().validate();

                // 如果是refresh bearer token api，就跳过对请求的认证
                if (!isRefreshBearerTokenAPI(request)) {
                    authApp.authenticate(new AuthCommand(accessToken.get().type.toString(), accessToken.get().accessToken));
                }

                RequestContext.putAccessToken(accessToken.get());
            }
        } catch (ApplicationException ex) {
            logger.error("Error while authenticate access token", ex);
            Responses.setStatusAndWriteTo(response, ex.errorId(), ex.getMessage());
            return false;
        } catch (Exception ex) {
            logger.error("Error while authenticate access token", ex);
            Responses.setStatusAndWriteTo(response, CommonErrorId.unauthorized, ex.getMessage());
            return false;
        } finally {
            if (logger.isInfoEnabled()) {
                logger.info("Finished authenticating access token");
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
