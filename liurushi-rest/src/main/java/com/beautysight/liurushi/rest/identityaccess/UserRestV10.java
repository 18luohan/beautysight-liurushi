/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.rest.identityaccess;

import com.beautysight.liurushi.common.utils.PreconditionUtils;
import com.beautysight.liurushi.identityaccess.app.user.*;
import com.beautysight.liurushi.identityaccess.domain.user.User;
import com.beautysight.liurushi.rest.common.APIs;
import com.beautysight.liurushi.rest.common.RequestContext;
import com.beautysight.liurushi.rest.permission.VisitorApiPermission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author chenlong
 * @since 1.0
 */
@RestController
@RequestMapping(APIs.USERS_V10)
public class UserRestV10 {

    private static final Logger logger = LoggerFactory.getLogger(UserRestV10.class);

    @Autowired
    private UserApp userApp;

    @RequestMapping(value = "/actions/exist", method = RequestMethod.GET)
    @VisitorApiPermission
    public ExistenceOfUserVM checkIfUserExistWith(@RequestParam String mobile) {
        PreconditionUtils.checkRequiredMobile("request param mobile", mobile);
        return userApp.checkIfUserExistWith(mobile);
    }

    @RequestMapping(value = "/{mobileOrUnionId}/actions/exist", method = RequestMethod.GET)
    @VisitorApiPermission
    public ExistenceOfUserVM checkIfUserExistWith(@PathVariable("mobileOrUnionId") String mobileOrUnionId,
                                                @RequestParam(required = false) User.Origin origin) {
        PreconditionUtils.checkRequired("path variable mobileOrUnionId", mobileOrUnionId);
        return userApp.checkIfUserExistWith(mobileOrUnionId, origin);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @VisitorApiPermission
    public UserAndAccessTokenVM signUp(@RequestBody SignUpCommand signUpCommand) {
        logger.info("New user sign up");
        signUpCommand.validate();
        return userApp.signUp(signUpCommand);
    }

    @RequestMapping(value = "/actions/login", method = RequestMethod.PUT)
    @VisitorApiPermission
    public UserAndAccessTokenVM login(@RequestBody LoginCommand loginCommand) {
        logger.info("User login");
        loginCommand.validate();
        return userApp.login(loginCommand);
    }

    @RequestMapping(value = "/actions/logout", method = RequestMethod.PUT)
    public void logout() {
        logger.info("User logout");
        userApp.logout(RequestContext.currentAccessToken().accessToken(),
                RequestContext.currentAccessToken().type());
    }

    @RequestMapping(value = "/{mobile}/actions/reset_pwd", method = RequestMethod.PUT)
    @VisitorApiPermission
    public void resetPassword(@PathVariable("mobile") String mobile, @RequestBody ResetPasswordCommand command) {
        command.mobile = mobile;
        command.validate();
        userApp.resetPassword(command);
    }

    @RequestMapping(value = "/actions/set_grp_to_pro", method = RequestMethod.PUT)
    @VisitorApiPermission
    public void setUsersGroupToProfessional(@RequestBody List<String> mobiles) {
        PreconditionUtils.checkRequired("request param mobiles", mobiles);
        userApp.setUsersGroupToProfessional(mobiles);
    }

}