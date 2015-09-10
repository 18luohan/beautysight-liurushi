/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.rest.social;

import com.beautysight.liurushi.community.app.WorkApp;
import com.beautysight.liurushi.community.app.presentation.WorkVM;
import com.beautysight.liurushi.rest.common.APIs;
import com.beautysight.liurushi.rest.common.Requests;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * @author chenlong
 * @since 1.0
 */
@RestController
@RequestMapping(APIs.SHARES_V1)
public class ShareRest {

    private static final Logger logger = LoggerFactory.getLogger(ShareRest.class);

    @Autowired
    private WorkApp workApp;

    @RequestMapping(value = "/works/{workId}", method = RequestMethod.GET)
    public WorkVM shareWork(@PathVariable("workId") String workId, HttpServletResponse response) {
//        response.setHeader("Access-Control-Allow-Origin", "*");
//        response.setHeader("Access-Control-Allow-Methods", "GET, HEAD, OPTIONS");
//        response.setHeader("Access-Control-Max-Age", "3600");
//        response.setHeader("Access-Control-Allow-Headers", "x-requested-with, " + Requests.REQUEST_ID);
        return workApp.shareWork(workId);
    }

}