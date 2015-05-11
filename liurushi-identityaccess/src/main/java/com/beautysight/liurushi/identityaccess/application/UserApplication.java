/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.application;

import com.beautysight.liurushi.identityaccess.application.command.LoginCommand;
import com.beautysight.liurushi.identityaccess.application.command.SignUpCommand;
import com.beautysight.liurushi.identityaccess.application.presentation.AccessTokenPresentation;
import org.springframework.stereotype.Service;

/**
 * Here is Javadoc.
 * <p/>
 * Created by chenlong on 2015-05-08.
 *
 * @author chenlong
 * @since 1.0
 */
@Service
public class UserApplication {

    public AccessTokenPresentation signUp(SignUpCommand command) {
        command.validate();
        return null;
    }

    public AccessTokenPresentation login(LoginCommand command) {
        command.validate();
        return null;
    }

}
