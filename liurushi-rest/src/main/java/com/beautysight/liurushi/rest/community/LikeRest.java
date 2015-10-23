/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.rest.community;

import com.beautysight.liurushi.community.app.LikeApp;
import com.beautysight.liurushi.community.app.command.LikeOrCancelCommand;
import com.beautysight.liurushi.rest.common.APIs;
import com.beautysight.liurushi.rest.common.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 收藏相关的API
 *
 * @author chenlong
 * @since 1.0
 */
@RestController
@RequestMapping(APIs.LIKES_V10)
public class LikeRest {

    @Autowired
    private LikeApp likeApp;

    @RequestMapping(value = "", method = RequestMethod.POST)
    public void like(@RequestBody LikeOrCancelCommand command) {
        command.setUserId(RequestContext.currentUserId());
        command.validate();
        likeApp.likeWork(command);
    }

    @RequestMapping(value = "", method = RequestMethod.PUT)
    public void cancelLikeOfWork(@RequestBody LikeOrCancelCommand command) {
        command.setUserId(RequestContext.currentUserId());
        command.validate();
        likeApp.cancelLikeOfWork(command);
    }

}
