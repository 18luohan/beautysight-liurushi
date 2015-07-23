/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.rest.common;

import com.beautysight.liurushi.common.ex.ApplicationException;
import com.beautysight.liurushi.common.ex.CommonErrorId;
import com.beautysight.liurushi.common.ex.Error;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
@ControllerAdvice
public class GlobalExceptionHandlingAdvice {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandlingAdvice.class);

    // TODO 如果该类执行时抛出异常，由谁负责最终捕获？

    @ExceptionHandler(ApplicationException.class)
    public ModelAndView onBusinessException(ApplicationException ex, HttpServletRequest request, HttpServletResponse response) {
        return handleException(ex, ex.errorId(), request, response);
    }

    @ExceptionHandler({IllegalArgumentException.class, HttpMessageNotReadableException.class})
    public ModelAndView onIllegalArgumentException(Throwable ex, HttpServletRequest request, HttpServletResponse response) {
        return handleException(ex, CommonErrorId.invalid_params, request, response);
    }

    @ExceptionHandler(Throwable.class)
    public ModelAndView handleOthers(Throwable ex, HttpServletRequest request, HttpServletResponse response) {
        return handleException(ex, CommonErrorId.internal_server_error, request, response);
    }

    private ModelAndView handleException(Throwable ex, Error.Id errorId, HttpServletRequest request, HttpServletResponse response) {
        logException(ex, request);
        Responses.setStatus(response, errorId);
        return modelAndView(ex, errorId);
    }

    private void logException(Throwable ex, HttpServletRequest request) {
        logger.error(String.format("Error while processing request: %s", Requests.methodAndURI(request)), ex);
    }

    private ModelAndView modelAndView(Throwable ex, Error.Id errorId) {
        Error error = Error.of(errorId, ex.getMessage());

        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put("id", error.getId());
        attributes.put("message", error.getMessage());

        MappingJackson2JsonView view = new MappingJackson2JsonView();
        view.setAttributesMap(attributes);

        ModelAndView mav = new ModelAndView();
        mav.setView(view);
        return mav;
    }

}