/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.rest.common;

import com.beautysight.liurushi.common.ex.ApplicationException;
import com.beautysight.liurushi.common.ex.AuthException;
import com.beautysight.liurushi.common.ex.CommonErrorId;
import com.beautysight.liurushi.common.utils.Logs;
import com.beautysight.liurushi.identityaccess.app.OAuthApp;
import com.beautysight.liurushi.identityaccess.app.command.AccessTokenDTO;
import com.beautysight.liurushi.identityaccess.domain.model.AccessToken;
import com.beautysight.liurushi.identityaccess.domain.model.UserClient;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Here is Javadoc.
 * <p/>
 * Created by chenlong on 2015-05-07.
 *
 * @author chenlong
 * @since 1.0
 */
public class AccessTokenChecker extends HandlerInterceptorAdapter {

    private static final Logger logger = LoggerFactory.getLogger(AccessTokenChecker.class);

    private static final String AUTHORIZATION = "Authorization";

    @Autowired
    private OAuthApp authApp;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            AccessTokenDTO accessToken = getAccessToken(request);

            /*
             * 严格检查注册、登录时使用的token类型，预防恶意攻击。
             * 退出时无需检查，因为如果使用basic token，就不允许访问该API。
             * 其他接口不检查，因为没有清楚明确的规则确定该使用何种token。
             */
            if (isSignUpOrLogin(request) && (accessToken.type == AccessToken.Type.Bearer)) {
                Logs.warn(logger, "someone uses bearer token for sign-up or login api");
                Responses.setStatusAndWriteTo(response, CommonErrorId.unauthorized,
                        "Should use basic token for sign-up or login api");
                return false;
            }

            Optional<UserClient> userClient = authApp.authenticate(accessToken);
            if (userClient.isPresent()) {
                RequestContext.putThisUserClient(userClient.get());
            }
        } catch (ApplicationException ex) {
            Logs.error(logger, ex, "Error while auth");
            Responses.setStatusAndWriteTo(response, ex.errorId(), ex.getMessage());
        } catch (Exception ex) {
            Logs.error(logger, ex, "Error while auth");
            Responses.setStatusAndWriteTo(response, CommonErrorId.unauthorized, ex.getMessage());
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        super.afterCompletion(request, response, handler, ex);
        RequestContext.clear();
    }

    private AccessTokenDTO getAccessToken(HttpServletRequest request) {
        Optional<String> authorization = Requests.getHeader(AUTHORIZATION, request);
        Logs.debug(logger, "Authorize request: {}, authorization: {}",
                Requests.methodAndURI(request), authorization.orNull());

        if (!authorization.isPresent()) {
            throw new AuthException("%s header required", AUTHORIZATION);
        }

        try {
            return parse(authorization.get());
        } catch (Exception e) {
            throw new AuthException(e,
                    "malformed authorization, expected: <Basic|Bearer> ${access_token}", AUTHORIZATION);
        }
    }

    private AccessTokenDTO parse(String authorization) {
        String[] data = authorization.split(" ");
        Preconditions.checkArgument((data.length == 2), "Authorization malformed");
        return new AccessTokenDTO(AccessToken.Type.valueOf(data[0]), data[1]);
    }

    private boolean isSignUpOrLogin(HttpServletRequest request) {
        boolean signUp = (HttpMethod.POST == HttpMethod.valueOf(request.getMethod())
                && request.getRequestURI().endsWith("/users"));
        boolean login = (HttpMethod.PUT == HttpMethod.valueOf(request.getMethod())
                && request.getRequestURI().endsWith("/users/actions/login"));
        return (signUp || login);
    }

}
