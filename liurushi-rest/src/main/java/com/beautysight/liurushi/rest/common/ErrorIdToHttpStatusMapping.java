/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.rest.common;

import com.beautysight.liurushi.common.ex.CommonErrorId;
import com.beautysight.liurushi.common.ex.Error;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * Here is Javadoc.
 * <p/>
 * Created by chenlong on 2015-05-08.
 *
 * @author chenlong
 * @since 1.0
 */
public class ErrorIdToHttpStatusMapping {

    private static final Map<Error.Id, HttpStatus> MAPPING = new HashMap<Error.Id, HttpStatus>();

    static {

        // 4xx Client Error
        MAPPING.put(CommonErrorId.bad_request, HttpStatus.BAD_REQUEST);
        MAPPING.put(CommonErrorId.invalid_params, HttpStatus.UNPROCESSABLE_ENTITY);
        MAPPING.put(CommonErrorId.unauthorized, HttpStatus.UNAUTHORIZED);

        // 5xx Server Error
        MAPPING.put(CommonErrorId.internal_server_error, HttpStatus.INTERNAL_SERVER_ERROR);
        MAPPING.put(CommonErrorId.service_unavailable, HttpStatus.SERVICE_UNAVAILABLE);

    }

    public static HttpStatus correspondingStatus(Error.Id errorId) {
        return MAPPING.get(errorId);
    }

}
