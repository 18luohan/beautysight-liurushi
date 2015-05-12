/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.rest.common;

import com.beautysight.liurushi.common.ex.BusinessException;
import com.beautysight.liurushi.common.ex.CommonErrorId;
import com.beautysight.liurushi.common.ex.Error;
import com.beautysight.liurushi.common.utils.Logs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
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

    @ExceptionHandler(BusinessException.class)
    public void onBusinessException(BusinessException ex, HttpServletRequest request) {
        handleException(ex, ex.errorId(), request);
    }

    @ExceptionHandler({IllegalArgumentException.class})
    public void onIllegalArgumentException(Throwable ex, HttpServletRequest request) {
        handleException(ex, CommonErrorId.invalid_params, request);
    }

    @ExceptionHandler(Throwable.class)
    public ModelAndView handleOthers(Throwable ex, HttpServletRequest request) {
        return handleException(ex, CommonErrorId.internal_server_error, request);
    }

    private ModelAndView handleException(Throwable ex, Error.Id errorId, HttpServletRequest request) {
        logException(ex, request);
        return modelAndView(ex, errorId);
    }

    private void logException(Throwable ex, HttpServletRequest request) {
        Logs.error(logger, ex, "Error while processing request: {}", Requests.methodAndURI(request));
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