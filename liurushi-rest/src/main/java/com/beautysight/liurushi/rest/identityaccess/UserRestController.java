/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.rest.identityaccess;

import com.beautysight.liurushi.identityaccess.application.UserApplication;
import com.beautysight.liurushi.identityaccess.application.command.LoginCommand;
import com.beautysight.liurushi.identityaccess.application.command.SignUpCommand;
import com.beautysight.liurushi.identityaccess.application.presentation.AccessTokenPresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Here is Javadoc.
 * <p/>
 * Created by chenlong on 2015-05-06.
 *
 * @author chenlong
 * @since 1.0
 */
@RestController
@RequestMapping("/users")
public class UserRestController {

    @Autowired
    private UserApplication userApp;

    @RequestMapping(value = "", method = RequestMethod.POST)
    public AccessTokenPresentation signUp(@RequestBody SignUpCommand signUpCommand) {
        return userApp.signUp(signUpCommand);
    }

    @RequestMapping(value = "/actions/login", method = RequestMethod.PUT)
    public AccessTokenPresentation login(@RequestBody LoginCommand loginCommand) {
        return userApp.login(loginCommand);
    }

}
