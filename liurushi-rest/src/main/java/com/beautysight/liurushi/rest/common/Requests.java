/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.rest.common;

import com.beautysight.liurushi.identityaccess.app.command.AccessTokenDPO;
import com.beautysight.liurushi.identityaccess.ex.AuthException;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * @author chenlong
 * @since 1.0
 */
public class Requests {

    private static final Logger logger = LoggerFactory.getLogger(Requests.class);

    public static final String REQUEST_ID = "X-Request-ID";
    public static final String AUTHORIZATION = "Authorization";
    public static final String APP_ID_HEADER = "X-BeautySight-App-ID";

    public static String methodAndURI(HttpServletRequest request) {
        return String.format("%s %s", request.getMethod(), request.getRequestURI());
    }

    public static Optional<String> requestIdOf(HttpServletRequest request) {
        return Requests.getHeader(REQUEST_ID, request);
    }

    public static Optional<String> appIdOf(HttpServletRequest request) {
        return Requests.getHeader(APP_ID_HEADER, request);
    }

    public static Optional<String> getHeader(String headerName, HttpServletRequest request) {
        String val = request.getHeader(headerName);
        return Optional.fromNullable(
                decodeWithUTF8(val, String.format("request header %s", headerName)));
    }

    public static String decodeWithUTF8(String data, String dataDescription) {
        if (StringUtils.isBlank(data)) {
            return null;
        }

        try {
            return URLDecoder.decode(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(String.format("Decode %s with UTF-8", dataDescription), e);
        }
    }

    public static Optional<AccessTokenDPO> getAccessToken(HttpServletRequest request) {
        Optional<String> authorization = Requests.getHeader(AUTHORIZATION, request);
        if (logger.isDebugEnabled()) {
            logger.debug("Got authorization header: {}", authorization.orNull());
        }

        if (authorization.isPresent()) {
            try {
                return Optional.of(parse(authorization.get()));
            } catch (Exception e) {
                throw new AuthException(e,
                        "malformed authorization, expected: <Basic|Bearer> ${access_token}", AUTHORIZATION);
            }
        }

        return Optional.absent();
    }

    private static AccessTokenDPO parse(String authorization) {
        String[] data = authorization.split(" ");
        Preconditions.checkArgument((data.length == 2), "Authorization malformed");
        return new AccessTokenDPO(data[0], data[1]);
    }

}