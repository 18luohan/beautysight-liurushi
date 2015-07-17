/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.rest.common;

import com.beautysight.liurushi.common.ex.AuthException;
import com.beautysight.liurushi.common.utils.Logs;
import com.beautysight.liurushi.interfaces.identityaccess.facade.dto.AccessTokenDTO;
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

    private static final String AUTHORIZATION = "Authorization";

    public static String methodAndURI(HttpServletRequest request) {
        return String.format("%s %s", request.getMethod(), request.getRequestURI());
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

    public static AccessTokenDTO getAccessToken(HttpServletRequest request) {
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

    private static AccessTokenDTO parse(String authorization) {
        String[] data = authorization.split(" ");
        Preconditions.checkArgument((data.length == 2), "Authorization malformed");
        return new AccessTokenDTO(AccessTokenDTO.Type.valueOf(data[0]), data[1]);
    }

}
