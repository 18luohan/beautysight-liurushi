/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.rest.identityaccess;

import com.beautysight.liurushi.identityaccess.app.OAuthApp;
import com.beautysight.liurushi.identityaccess.app.command.DeviceDTO;
import com.beautysight.liurushi.identityaccess.app.presentation.AccessTokenPresentation;
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
@RequestMapping("/oauth")
public class OAuthRest {

    @Autowired
    private OAuthApp oAuthApp;

    @RequestMapping(value = "/basic_token", method = RequestMethod.POST)
    @VisitorApiPermission(true)
    public AccessTokenPresentation getIfAbsentIssueBasicTokenFor(@RequestBody DeviceDTO deviceDTO) {
        deviceDTO.validate();
        return oAuthApp.getIfAbsentIssueBasicTokenFor(deviceDTO);
    }

}
