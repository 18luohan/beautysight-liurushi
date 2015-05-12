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
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Here is Javadoc.
 * <p/>
 * Created by chenlong on 2015-05-07.
 *
 * @author chenlong
 * @since 1.0
 */
public class GlobalHandlerExceptionResolver implements HandlerExceptionResolver {

    private static final Logger logger = LoggerFactory.getLogger(GlobalHandlerExceptionResolver.class);

    // TODO 如果该类执行时抛出异常，由谁负责最终捕获？

    @Override
    public ModelAndView resolveException(HttpServletRequest request,
                                         HttpServletResponse response, Object handler, Exception ex) {
        Logs.error(logger, ex, "Error while processing request: {}", Requests.methodAndURI(request));
        return modelAndView(error(ex));
    }

    private ModelAndView modelAndView(Error error) {
        Map<String,Object> attributes = new HashMap<String,Object>();
        attributes.put("id", error.getId());
        attributes.put("message", error.getMessage());

        MappingJackson2JsonView view = new MappingJackson2JsonView();
        view.setAttributesMap(attributes);

        ModelAndView mav = new ModelAndView();
        mav.setView(view);
        return mav;
    }

    private Error error(Throwable ex) {
        Error.Id errorId;
        if (ex instanceof BusinessException) {
            errorId = ((BusinessException) ex).errorId();
        } else {
            errorId = CommonErrorId.internal_server_error;
        }

        return Error.of(errorId, ex.getMessage());
    }

}
