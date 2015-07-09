/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.rest.permission;

import com.beautysight.liurushi.common.ex.CommonErrorId;
import com.beautysight.liurushi.common.utils.Logs;
import com.beautysight.liurushi.rest.common.RequestContext;
import com.beautysight.liurushi.rest.common.Requests;
import com.beautysight.liurushi.rest.common.Responses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Here is Javadoc.
 * <p/>
 * Created by chenlong on 2015-05-30.
 *
 * @author chenlong
 * @since 1.0
 */
public class ApiPermissionInterceptor extends HandlerInterceptorAdapter {

    private static final Logger logger = LoggerFactory.getLogger(ApiPermissionInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        boolean hasPermission = hasPermission(request, handler);
        if (hasPermission) {
            return true;
        }

        Responses.setStatusAndWriteTo(response, CommonErrorId.no_permission_for_this_api,
                "You have no permission for this api");
        return false;
    }

    private boolean hasPermission(HttpServletRequest request, Object handler) {
        if (RequestContext.isThisUserAMember()) {
            return true;
        }

        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            VisitorApiPermission annotation = handlerMethod.getMethodAnnotation(VisitorApiPermission.class);

            if (annotation == null) {
                if (logger.isDebugEnabled()) {
                    Logs.debug(logger, "{}.{} not annotated by {}, so visitors can not request this api",
                            handlerMethod.getBeanType().getSimpleName(),
                            handlerMethod.getMethod().getName(),
                            VisitorApiPermission.class.getSimpleName());
                }
                return false;
            }

            return annotation.value();
        }

        if (logger.isWarnEnabled()) {
            Logs.warn(logger, "Give permission for api {}, because it'll be processed by a handler, rather than a handler method.",
                    Requests.methodAndURI(request));
        }

        return true;
    }

}
