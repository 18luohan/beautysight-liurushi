/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.rest.common;

import com.beautysight.liurushi.common.ex.BadRequestException;
import com.beautysight.liurushi.common.ex.CommonErrorId;
import com.beautysight.liurushi.common.utils.Logs;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            AccessToken accessToken = bearerAccessToken(request, response);
        } catch (BadRequestException ex) {
            Responses.setStatusAndWriteTo(response, ex.errorId(), ex.getMessage());
        }

        // TODO 校验 access token 的合法性、有效性
        // TODO 根据 access token 获取用户信息，缓存至某个地方
        return true;
    }

    private AccessToken bearerAccessToken(HttpServletRequest request, HttpServletResponse response) {
        Optional<String> authorization = Requests.getHeader(AUTHORIZATION, request);
        Logs.debug(logger, "Authorize request: {}, authorization: {}",
                Requests.methodAndURI(request), authorization.orNull());

        if (!authorization.isPresent()) {
            throw new BadRequestException(CommonErrorId.unauthorized,
                    String.format("%s header required", AUTHORIZATION));
        }

        try {
            return new AccessToken(authorization.get());
        } catch (Exception e) {
            throw new BadRequestException(CommonErrorId.unauthorized,
                    String.format("malformed authorization, expected: Bearer ${access_token}", AUTHORIZATION));
        }
    }

    private static class AccessToken {
        private Type type;
        private String val;

        private AccessToken(String authorization) {
            String[] data = authorization.split(" ");
            Preconditions.checkArgument((data.length == 2), "authorization malformed");
            this.type = Type.valueOf(data[0]);
            this.val = data[1];
        }

        private static enum Type {
            Bearer
        }
    }

}
