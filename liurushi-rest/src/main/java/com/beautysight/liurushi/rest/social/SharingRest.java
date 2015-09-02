/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.rest.social;

import com.beautysight.liurushi.community.app.WorkApp;
import com.beautysight.liurushi.community.app.dpo.PictureStoryDPO;
import com.beautysight.liurushi.rest.common.APIs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chenlong
 * @since 1.0
 */
@RestController
@RequestMapping(APIs.SHARING_V1)
public class SharingRest {

    @Autowired
    private WorkApp workApp;

    @RequestMapping(value = "/h5/{workId}", method = RequestMethod.GET)
    public PictureStoryDPO h5SharingOf(@PathVariable("workId") String workId) {
        return workApp.h5SharingOf(workId);
    }

}