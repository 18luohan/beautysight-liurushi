/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.rest.common;

import com.beautysight.liurushi.common.ex.ApplicationException;
import com.beautysight.liurushi.common.ex.CommonErrorId;
import com.beautysight.liurushi.common.utils.Logs;
import com.beautysight.liurushi.identityaccess.app.OAuthApp;
import com.beautysight.liurushi.identityaccess.app.UserApp;
import com.beautysight.liurushi.identityaccess.app.command.AccessTokenDPO;
import com.beautysight.liurushi.identityaccess.app.command.AuthCommand;
import com.beautysight.liurushi.identityaccess.domain.model.UserLite;
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
            Optional<AccessTokenDPO> accessToken = Requests.getAccessToken(request);
            if (logger.isDebugEnabled()) {
                logger.debug("Authenticating access token: {}", accessToken.orNull());
            }

            if (accessToken.isPresent()) {
                accessToken.get().validate();

                // 如果是refresh bearer token api，就跳过对请求的认证
                if (!isRefreshBearerTokenAPI(request)) {
                    authApp.authenticate(new AuthCommand(accessToken.get().type, accessToken.get().accessToken));
                }

                RequestContext.putAccessToken(accessToken.get());
                UserLite userLite = userApp.getCurrentUserProfile(accessToken.get().type, accessToken.get().accessToken);
                RequestContext.putUserProfile(userLite);
                // 将当前会话所属用户的id放入请求日志上下文中
                Logs.putUserId(userLite.id().toString());
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
