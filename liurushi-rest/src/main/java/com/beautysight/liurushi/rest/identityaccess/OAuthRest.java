/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.rest.identityaccess;

import com.beautysight.liurushi.identityaccess.app.OAuthApp;
import com.beautysight.liurushi.identityaccess.app.command.RefreshAccessTokenCommand;
import com.beautysight.liurushi.identityaccess.app.presentation.AccessTokenPresentation;
import com.beautysight.liurushi.rest.common.APIs;
import com.beautysight.liurushi.rest.common.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author chenlong
 * @since 1.0
 */
@RestController
@RequestMapping(APIs.OAUTH_V1)
public class OAuthRest {

    @Autowired
    private OAuthApp oAuthApp;

    @RequestMapping(value = "/bearer_token", method = RequestMethod.PUT)
    public AccessTokenPresentation refreshBearerToken(@RequestBody RefreshAccessTokenCommand command,
                                                      HttpServletRequest request) {
        command.bearerToken = RequestContext.getAccessToken().accessToken;
        command.validate();
        return oAuthApp.refreshBearerToken(command);
    }

}
