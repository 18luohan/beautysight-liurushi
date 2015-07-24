/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.rest.identityaccess;

import com.beautysight.liurushi.common.utils.PreconditionUtils;
import com.beautysight.liurushi.fundamental.app.DownloadUrlPresentation;
import com.beautysight.liurushi.identityaccess.app.OAuthApp;
import com.beautysight.liurushi.identityaccess.app.UserApp;
import com.beautysight.liurushi.identityaccess.app.command.LoginCommand;
import com.beautysight.liurushi.identityaccess.app.command.SignUpCommand;
import com.beautysight.liurushi.identityaccess.app.presentation.SignUpOrLoginPresentation;
import com.beautysight.liurushi.identityaccess.app.presentation.UserExistPresentation;
import com.beautysight.liurushi.identityaccess.app.presentation.UserProfilePresentation;
import com.beautysight.liurushi.rest.common.APIs;
import com.beautysight.liurushi.rest.common.RequestContext;
import com.beautysight.liurushi.rest.permission.VisitorApiPermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @RequestMapping(value = "/actions/exist", method = RequestMethod.GET)
    @VisitorApiPermission(true)
    public UserExistPresentation checkIfUserExistWith(@RequestParam String mobile) {
        PreconditionUtils.checkRequiredMobile("request param mobile", mobile);
        return userApp.checkIfUserExistWith(mobile);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @VisitorApiPermission(true)
    public SignUpOrLoginPresentation signUp(@RequestBody SignUpCommand signUpCommand) {
        signUpCommand.validate();
        return userApp.signUp(signUpCommand);
    }

    @RequestMapping(value = "/actions/login", method = RequestMethod.PUT)
    @VisitorApiPermission(true)
    public SignUpOrLoginPresentation login(@RequestBody LoginCommand loginCommand) {
        loginCommand.validate();
        return userApp.login(loginCommand);
    }

    @RequestMapping(value = "/actions/logout", method = RequestMethod.PUT)
    public void logout() {
        // TODO 让 token 过期
//        userApp.logout(oAuthApp.getUserClientBy(
//                RequestContext.getAccessToken().type.toString(),
//                RequestContext.getAccessToken().accessToken));
    }

    @RequestMapping(value = "/current", method = RequestMethod.GET)
    public UserProfilePresentation getCurrentUserProfile() {
        return userApp.getCurrentUserProfile(
                RequestContext.getAccessToken().type.toString(),
                RequestContext.getAccessToken().accessToken);
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    @VisitorApiPermission(true)
    public UserProfilePresentation getUserProfile(String userId) {
        PreconditionUtils.checkRequired("url path variable userId", userId);
        return userApp.getUserProfile(userId);
    }


    @RequestMapping(value = "/current/avatar/max", method = RequestMethod.POST)
    public DownloadUrlPresentation issueDownloadUrlOfMaxAvatar() {
        return userApp.issueDownloadUrlOfMaxAvatar(
                oAuthApp.getUserClientBy(RequestContext.getAccessToken().type.toString(),
                        RequestContext.getAccessToken().accessToken));
    }

    @RequestMapping(value = "/actions/set_grp_to_pro", method = RequestMethod.PUT)
    @VisitorApiPermission(true)
    public void setUsersGroupToProfessional(@RequestBody List<String> mobiles) {
        PreconditionUtils.checkRequired("request param mobiles", mobiles);
        userApp.setUsersGroupToProfessional(mobiles);
    }

}