/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.application.command;

import com.beautysight.liurushi.common.application.Command;
import com.beautysight.liurushi.common.utils.PreconditionUtils;
import com.google.common.base.Preconditions;

/**
 * Here is Javadoc.
 * <p/>
 * Created by chenlong on 2015-05-08.
 *
 * @author chenlong
 * @since 1.0
 */
public class SignUpCommand implements Command {

    private UserDTO user;
    private DeviceDTO device;

    public void validate() {
        PreconditionUtils.checkRequired("user", user);
        PreconditionUtils.checkRequired("device", device);
        validateUser();
        device.validate();
    }

    private void validateUser() {
        PreconditionUtils.checkRequired("user.nickname", user.nickname);
        PreconditionUtils.checkRequired("user.mobilePhone", user.mobilePhone);
        PreconditionUtils.checkRequired("user.password", user.password);
        PreconditionUtils.checkRequired("user.confirmPassword", user.confirmPassword);
        Preconditions.checkArgument(user.password.equals(user.confirmPassword),
                "user password not equal to confirmPassword");
    }

}
