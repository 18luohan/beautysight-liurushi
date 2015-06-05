/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.rest.common;

import com.beautysight.liurushi.common.ex.CommonErrorId;
import com.beautysight.liurushi.common.utils.Logs;
import com.google.common.base.Optional;
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
public class RequestIdChecker extends HandlerInterceptorAdapter {

    private static final Logger logger = LoggerFactory.getLogger(RequestIdChecker.class);

    private static final String REQUEST_ID = "X-Request-ID";

    /**
     * Intercept the execution of a handler. Called after HandlerMapping determined
     * an appropriate handler object, but before HandlerAdapter invokes the handler.
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Optional<String> requestId = Requests.getHeader(REQUEST_ID, request);
        Logs.debugWithoutPrefixTraceId(logger, "Receive request: {}, request id: {}",
                Requests.methodAndURI(request), requestId.orNull());

        if (!requestId.isPresent()) {
            Responses.setStatusAndWriteTo(response, CommonErrorId.bad_request,
                    String.format("%s header required", REQUEST_ID));
            return false;
        }

        Logs.setTraceId(requestId.get());
        return true;
    }

    /*
     * HandlerInterceptor.postHandle method:
     * Intercept the execution of a handler. Called after HandlerAdapter actually
     * invoked the handler, but before the DispatcherServlet renders the view.
     * Can expose additional model objects to the view via the given ModelAndView.
     */

    /**
     * Callback after completion of request processing, that is, after rendering
     * the view. Will be called on any outcome of handler execution, thus allows
     * for proper resource cleanup.
     * <p>Note: Will only be called if this interceptor's {@code preHandle}
     * method has successfully completed and returned {@code true}!
     *
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        super.afterCompletion(request, response, handler, ex);
        // 注意，如果preHandle方法返回false或抛出异常，该方法就不会执行
        // TODO 如果HandlerInterceptor中抛出异常，该由谁来负责捕获处理这些异常？
        // TODO 如果Controller(对spring来说就是handler)中抛出异常，该在哪里执行清理操作？
        Logs.clearTraceId();
    }
}
