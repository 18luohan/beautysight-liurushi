/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.rest.identityaccess;

import com.beautysight.liurushi.fundamental.app.DownloadUrlPresentation;
import com.beautysight.liurushi.fundamental.app.FileMetadataDPO;
import com.beautysight.liurushi.identityaccess.app.UserApp;
import com.beautysight.liurushi.identityaccess.app.presentation.PersonalCenter;
import com.beautysight.liurushi.rest.common.APIs;
import com.beautysight.liurushi.rest.permission.VisitorApiPermission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author chenlong
 * @since 1.0
 */
@RestController
@RequestMapping(APIs.USERS_V1)
public class UserPersonalCenterRest {

    private static final Logger logger = LoggerFactory.getLogger(UserRest.class);

    @Autowired
    private UserApp userApp;

    @RequestMapping(value = "/{userId}/header_photo", method = RequestMethod.PUT)
    public DownloadUrlPresentation changeMyHeaderPhoto(@PathVariable("userId") String userId,
                                                       @RequestBody FileMetadataDPO newHeaderPhoto) {
        newHeaderPhoto.validate();
        return userApp.changeUserHeaderPhoto(userId, newHeaderPhoto);
    }

    @RequestMapping(value = "/{userId}/personal_center", method = RequestMethod.GET)
    @VisitorApiPermission(true)
    public PersonalCenter getUserPersonalCenter(@PathVariable("userId") String userId) {
        return userApp.getUserPersonalCenter(userId);
    }

    @RequestMapping(value = "/{userId}/works", method = RequestMethod.GET)
    @VisitorApiPermission(true)
    public List<Map<String, Object>> myWorks(@PathVariable("userId") String userId) {
        return new ArrayList<>();
    }

    @RequestMapping(value = "/{userId}/favorites", method = RequestMethod.GET)
    @VisitorApiPermission(true)
    public List<Map<String, Object>> myFavorites(@PathVariable("userId") String userId) {
        return new ArrayList<>();
    }

}
