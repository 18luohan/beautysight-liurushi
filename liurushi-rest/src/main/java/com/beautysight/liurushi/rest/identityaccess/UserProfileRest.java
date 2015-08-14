/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.rest.identityaccess;

import com.beautysight.liurushi.fundamental.app.DownloadUrlPresentation;
import com.beautysight.liurushi.fundamental.app.FileMetadataDPO;
import com.beautysight.liurushi.identityaccess.app.UserApp;
import com.beautysight.liurushi.identityaccess.app.presentation.UserDPO;
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

    @RequestMapping(value = "/current", method = RequestMethod.GET)
    public UserDPO myProfile() {
        return userApp.getUserProfile(RequestContext.getUser().id().toString());
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    @VisitorApiPermission(true)
    public UserDPO getUserProfile(@PathVariable("userId") String userId) {
        return userApp.getUserProfile(userId);
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.PUT)
    public UserDPO editUserProfile(@PathVariable("userId") String userId,
                                          @RequestBody UserDPO userDPO) {
        userDPO.id = userId;
        return userApp.editUserProfile(userDPO);
    }

    @RequestMapping(value = "/{userId}/avatar", method = RequestMethod.PUT)
    public DownloadUrlPresentation changeUserAvatar(@PathVariable("userId") String userId,
                                                    @RequestBody FileMetadataDPO newAvatar) {
        newAvatar.validate();
        return userApp.changeUserOriginalAvatar(userId, newAvatar);
    }

}
