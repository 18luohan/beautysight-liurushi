/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.rest.identityaccess;

import com.beautysight.liurushi.fundamental.app.DownloadUrl;
import com.beautysight.liurushi.fundamental.app.FileMetadataPayload;
import com.beautysight.liurushi.identityaccess.app.user.UserApp;
import com.beautysight.liurushi.identityaccess.domain.user.UserView;
import com.beautysight.liurushi.identityaccess.domain.user.pl.UserPayload;
import com.beautysight.liurushi.rest.common.APIs;
import com.beautysight.liurushi.rest.common.RequestContext;
import com.beautysight.liurushi.rest.permission.VisitorApiPermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author chenlong
 * @since 1.0
 */
@RestController
@RequestMapping(APIs.USERS_V1)
public class UserProfileRest {

    @Autowired
    private UserApp userApp;

//    @RequestMapping(value = "/current", method = RequestMethod.GET)
//    public UserView.Whole myProfile() {
//        return userApp.getUserProfile(RequestContext.getUser().getId());
//    }

//    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
//    @VisitorApiPermission
//    public UserView.Whole getUserProfile(@PathVariable("userId") String userId) {
//        return userApp.getUserProfile(userId);
//    }


}
