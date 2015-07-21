/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.rest.common;

import com.beautysight.liurushi.common.ex.CommonErrorId;
import com.beautysight.liurushi.common.ex.Error;
import com.beautysight.liurushi.common.utils.Logs;
import com.beautysight.liurushi.identityaccess.common.AuthErrorId;
import com.beautysight.liurushi.identityaccess.common.UserErrorId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(ErrorIdToHttpStatusMapping.class);

    private static final Map<Error.Id, HttpStatus> MAPPING = new HashMap<Error.Id, HttpStatus>();

    static {

        // 4xx Client Error
        MAPPING.put(CommonErrorId.bad_request, HttpStatus.BAD_REQUEST);
        MAPPING.put(CommonErrorId.invalid_params, HttpStatus.UNPROCESSABLE_ENTITY);
        MAPPING.put(CommonErrorId.unauthorized, HttpStatus.UNAUTHORIZED);
        MAPPING.put(CommonErrorId.no_permission_for_this_api, HttpStatus.FORBIDDEN);
        MAPPING.put(CommonErrorId.business_constraint_violated, HttpStatus.UNPROCESSABLE_ENTITY);

        MAPPING.put(AuthErrorId.invalid_app_id, HttpStatus.UNAUTHORIZED);
        MAPPING.put(AuthErrorId.invalid_access_token, HttpStatus.UNAUTHORIZED);
        MAPPING.put(AuthErrorId.expired_access_token, HttpStatus.UNAUTHORIZED);

        MAPPING.put(UserErrorId.password_confirmpwd_not_equal, HttpStatus.UNPROCESSABLE_ENTITY);
        MAPPING.put(UserErrorId.user_already_exist, HttpStatus.CONFLICT);
        MAPPING.put(UserErrorId.user_not_exist_or_pwd_incorrect, HttpStatus.UNPROCESSABLE_ENTITY);

        // 5xx Server Error
        MAPPING.put(CommonErrorId.internal_server_error, HttpStatus.INTERNAL_SERVER_ERROR);
        MAPPING.put(CommonErrorId.service_unavailable, HttpStatus.SERVICE_UNAVAILABLE);

        MAPPING.put(CommonErrorId.server_data_stale, HttpStatus.SERVICE_UNAVAILABLE);

    }

    public static HttpStatus correspondingStatus(Error.Id errorId) {
        HttpStatus status = MAPPING.get(errorId);
        if (status == null) {
            Logs.error(logger, "No corresponding http status to error id: {}", errorId);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return status;
    }

}
