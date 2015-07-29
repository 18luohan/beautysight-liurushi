/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.rest.common;

import com.beautysight.liurushi.common.utils.AsyncTasks;
import com.beautysight.liurushi.common.utils.Beans;
import com.beautysight.liurushi.common.utils.Logs;
import com.beautysight.liurushi.common.utils.RequestLogContext;
import com.beautysight.liurushi.identityaccess.domain.model.RequestLog;
import com.beautysight.liurushi.identityaccess.domain.repo.RequestLogRepo;
import com.google.common.base.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Reference to {@code ch.qos.logback.classic.helpers.MDCInsertingServletFilter}.
 *
 * @author chenlong
 * @since 1.0
 */
public class MDCInsertingServletFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(MDCInsertingServletFilter.class);

    private static RequestLogRepo requestLogRepo;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        WebApplicationContext appCtx = WebApplicationContextUtils.getWebApplicationContext(
                filterConfig.getServletContext());
        requestLogRepo = appCtx.getBean("requestLogRepo", RequestLogRepo.class);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        preHandle(request);
        try {
            chain.doFilter(request, response);
        } finally {
            postHandle();
        }
    }

    @Override
    public void destroy() {
        // do nothing
    }

    private void preHandle(ServletRequest request) {
        if (!isHttpReq(request)) {
            String message = String.format("Expected http request, but actual not: %s", request.getClass());
            logger.error(message);
            throw new RuntimeException(message);
        }

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        insertIntoMDC(httpRequest);
        putIntoReqLogContext(httpRequest);

//        final RequestLogContext context = RequestLogContext.copyReqLogCtxForCurrentReq();
//        AsyncTasks.submit(new Runnable() {
//            @Override
//            public void run() {
//                RequestLog targetRequestLog = new RequestLog();
//                Beans.copyProperties(context, targetRequestLog);
//                RequestLog savedLog = requestLogRepo.save(targetRequestLog);
//                // 不能在这里操作当前请求日志上下文，因为它绑定在容器worker线程中，而不是这个线程
//                //RequestLogContext.setId(savedLog.idString());
//            }
//        });
    }

    private void postHandle() {
        RequestLogContext.setCostOfTimeUntilNow();

        final RequestLogContext context = RequestLogContext.copyReqLogCtxForCurrentReq();
        AsyncTasks.submit(new Runnable() {
            @Override
            public void run() {
                RequestLog targetRequestLog = new RequestLog();
                Beans.copyProperties(context, targetRequestLog);
                requestLogRepo.save(targetRequestLog);
            }
        });
        clearMDC();
        clearReqLogContext();
    }

    private void insertIntoMDC(HttpServletRequest httpRequest) {
        String requestId = getRequestId(httpRequest);
        Logs.putReqId(requestId);

        if (logger.isInfoEnabled()) {
            logger.info("Receive request id: {}, uri: {}",
                    requestId, Requests.methodAndURI(httpRequest));
        }
    }

    private void clearMDC() {
        Logs.clearMDC();
    }

    private void putIntoReqLogContext(HttpServletRequest httpRequest) {
        RequestLogContext.putReqId(getRequestId(httpRequest));
        RequestLogContext.putHttpInfo(Requests.methodAndURI(httpRequest),
                httpRequest.getQueryString(), httpRequest.getProtocol());
    }

    private void clearReqLogContext() {
        RequestLogContext.clear();
    }

    private String getRequestId(HttpServletRequest request) {
        Optional<String> requestId = Requests.requestIdOf(request);
        return requestId.isPresent() ? requestId.get() : "absent";
    }

    private boolean isHttpReq(ServletRequest request) {
        return (request instanceof HttpServletRequest);
    }

}
