/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.rest.identityaccess;

import com.beautysight.liurushi.fundamental.app.DownloadUrl;
import com.beautysight.liurushi.fundamental.app.FileMetadataPayload;
import com.beautysight.liurushi.identityaccess.app.user.PersonalCenterVM;
import com.beautysight.liurushi.identityaccess.app.user.UserApp;
import com.beautysight.liurushi.identityaccess.domain.user.UserView;
import com.beautysight.liurushi.identityaccess.domain.user.pl.UserPayload;
import com.beautysight.liurushi.rest.common.APIs;
import com.beautysight.liurushi.rest.common.RequestContext;
import com.beautysight.liurushi.rest.permission.VisitorApiPermission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author chenlong
 * @since 1.0
 */
@RestController
@RequestMapping(APIs.USERS_V1)
public class PersonalCenterRest {

    private static final Logger logger = LoggerFactory.getLogger(UserRest.class);

    @Autowired
    private UserApp userApp;

    @RequestMapping(value = "/{userId}/personal_center", method = RequestMethod.GET)
    @VisitorApiPermission
    public PersonalCenterVM getUserPersonalCenter(@PathVariable("userId") String userId) {
        return userApp.getUserPersonalCenter(userId, RequestContext.optionalCurrentUserId());
    }

    @RequestMapping(value = "/{userId}/header_photo", method = RequestMethod.PUT)
    public DownloadUrl changeMyHeaderPhoto(@PathVariable("userId") String userId,
                                                       @RequestBody FileMetadataPayload newHeaderPhoto) {
        newHeaderPhoto.validate();
        return userApp.changeUserHeaderPhoto(userId, newHeaderPhoto);
    }

    @RequestMapping(value = "/{userId}/avatar", method = RequestMethod.PUT)
    public DownloadUrl changeUserAvatar(@PathVariable("userId") String userId,
                                        @RequestBody FileMetadataPayload newAvatar) {
        newAvatar.validate();
        return userApp.changeUserOriginalAvatar(userId, newAvatar);
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.PUT)
    public UserView.Whole editUserProfile(@PathVariable("userId") String userId,
                                          @RequestBody UserPayload userDPO) {
        userDPO.id = userId;
        return userApp.editUserProfile(userDPO);
    }

}
