/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.rest.common;

import com.google.common.base.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

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

    private static final String REQUEST_ID = "req.id";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // do nothing
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        insertIntoMDC(request);
        try {
            chain.doFilter(request, response);
        } finally {
            clearMDC();
        }
    }

    @Override
    public void destroy() {
        // do nothing
    }

    private void insertIntoMDC(ServletRequest request) {
        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            Optional<String> requestId = Requests.requestIdOf(httpRequest);
            String theId = requestId.isPresent() ? requestId.get() : "absent";
            MDC.put(REQUEST_ID, theId);

            if (logger.isInfoEnabled()) {
                logger.info("Receive request id: {}, uri: {}",
                        theId, Requests.methodAndURI(httpRequest));
            }
        }
//        MDC.put(ClassicConstants.REQUEST_REMOTE_HOST_MDC_KEY, request
//                .getRemoteHost());
//
//        if (request instanceof HttpServletRequest) {
//            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
//            MDC.put(ClassicConstants.REQUEST_REQUEST_URI, httpServletRequest
//                    .getRequestURI());
//            StringBuffer requestURL = httpServletRequest.getRequestURL();
//            if (requestURL != null) {
//                MDC.put(ClassicConstants.REQUEST_REQUEST_URL, requestURL.toString());
//            }
//            MDC.put(ClassicConstants.REQUEST_METHOD, httpServletRequest.getMethod());
//            MDC.put(ClassicConstants.REQUEST_QUERY_STRING, httpServletRequest.getQueryString());
//            MDC.put(ClassicConstants.REQUEST_USER_AGENT_MDC_KEY, httpServletRequest
//                    .getHeader("User-Agent"));
//            MDC.put(ClassicConstants.REQUEST_X_FORWARDED_FOR, httpServletRequest
//                    .getHeader("X-Forwarded-For"));
//        }
    }

    private void clearMDC() {
        MDC.remove(REQUEST_ID);
//        MDC.remove(ClassicConstants.REQUEST_REMOTE_HOST_MDC_KEY);
//        MDC.remove(ClassicConstants.REQUEST_REQUEST_URI);
//        MDC.remove(ClassicConstants.REQUEST_QUERY_STRING);
//        // removing possibly inexistent item is OK
//        MDC.remove(ClassicConstants.REQUEST_REQUEST_URL);
//        MDC.remove(ClassicConstants.REQUEST_METHOD);
//        MDC.remove(ClassicConstants.REQUEST_USER_AGENT_MDC_KEY);
//        MDC.remove(ClassicConstants.REQUEST_X_FORWARDED_FOR);
    }

}
