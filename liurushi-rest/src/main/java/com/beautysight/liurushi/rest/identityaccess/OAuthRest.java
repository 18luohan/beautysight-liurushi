/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.rest.identityaccess;

import com.beautysight.liurushi.identityaccess.app.auth.OAuthApp;
import com.beautysight.liurushi.identityaccess.app.auth.RefreshAccessTokenCommand;
import com.beautysight.liurushi.identityaccess.domain.auth.AccessTokenVM;
import com.beautysight.liurushi.rest.common.APIs;
import com.beautysight.liurushi.rest.common.RequestContext;
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
@RequestMapping(APIs.OAUTH_V1)
public class OAuthRest {

    @Autowired
    private OAuthApp oAuthApp;

    @RequestMapping(value = "/bearer_token", method = RequestMethod.PUT)
    public AccessTokenVM refreshBearerToken(@RequestBody RefreshAccessTokenCommand command) {
        command.accessToken = RequestContext.currentAccessToken().accessToken();
        command.validate();
        return oAuthApp.refreshAccessToken(command);
    }

}
