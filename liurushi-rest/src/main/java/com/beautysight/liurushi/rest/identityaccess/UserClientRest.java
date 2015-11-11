/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.rest.identityaccess;

import com.beautysight.liurushi.identityaccess.app.user.UserClientApp;
import com.beautysight.liurushi.identityaccess.app.user.UserClientCheckInPO;
import com.beautysight.liurushi.identityaccess.app.user.UserClientCheckInRO;
import com.beautysight.liurushi.rest.common.APIs;
import com.beautysight.liurushi.rest.permission.VisitorApiPermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chenlong
 * @since 1.0
 */
@RestController
@RequestMapping(APIs.USER_CLIENTS_V10)
public class UserClientRest {

    @Autowired
    private UserClientApp userClientApp;

    @RequestMapping(value = "/checkin", method = RequestMethod.POST)
    @VisitorApiPermission
    public UserClientCheckInRO signUp(@RequestBody UserClientCheckInPO checkInPO) {
        checkInPO.validate();
        return userClientApp.checkIn(checkInPO);
    }

}
