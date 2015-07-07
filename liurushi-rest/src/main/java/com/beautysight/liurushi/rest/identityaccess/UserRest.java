/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.rest.identityaccess;

import com.beautysight.liurushi.common.utils.PreconditionUtils;
import com.beautysight.liurushi.identityaccess.app.OAuthApp;
import com.beautysight.liurushi.identityaccess.app.UserApp;
import com.beautysight.liurushi.identityaccess.app.command.LoginCommand;
import com.beautysight.liurushi.identityaccess.app.command.SignUpCommand;
import com.beautysight.liurushi.identityaccess.app.presentation.AccessTokenPresentation;
import com.beautysight.liurushi.identityaccess.app.presentation.DownloadUrlPresentation;
import com.beautysight.liurushi.identityaccess.app.presentation.UserExistPresentation;
import com.beautysight.liurushi.identityaccess.domain.model.User;
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
public class UserRest {

    @Autowired
    private UserApp userApp;
    @Autowired
    private OAuthApp oAuthApp;

    @RequestMapping(value = "/{mobile}", method = RequestMethod.GET)
    @VisitorApiPermission(true)
    public UserExistPresentation checkIfUserExistWith(@PathVariable String mobile) {
        PreconditionUtils.checkRequiredMobile("mobile", mobile);
        return userApp.checkIfUserExistWith(mobile);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @VisitorApiPermission(true)
    public AccessTokenPresentation signUp(@RequestBody SignUpCommand signUpCommand) {
        signUpCommand.validate();
        return userApp.signUp(signUpCommand);
    }

    @RequestMapping(value = "/actions/login", method = RequestMethod.PUT)
    @VisitorApiPermission(true)
    public AccessTokenPresentation login(@RequestBody LoginCommand loginCommand) {
        loginCommand.validate();
        return userApp.login(loginCommand);
    }

    @RequestMapping(value = "/actions/logout", method = RequestMethod.PUT)
    public AccessTokenPresentation logout() {
        return userApp.logout(oAuthApp.getUserClientBy(RequestContext.getAccessToken()));
    }

    @RequestMapping(value = "/current/avatar/{avatarSpec}", method = RequestMethod.POST)
    public DownloadUrlPresentation issueDownloadAvatarUrl(@PathVariable int avatarSpec) {
        User.Avatar.validateSpec(avatarSpec);
        return userApp.issueDownloadAvatarUrl(avatarSpec, oAuthApp.getUserClientBy(RequestContext.getAccessToken()));
    }

}
