/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.app.command;

import com.beautysight.liurushi.common.app.Command;
import com.beautysight.liurushi.common.utils.PreconditionUtils;
import com.beautysight.liurushi.identityaccess.app.presentation.UserDPO;

/**
 * @author chenlong
 * @since 1.0
 */
public class LoginCommand implements Command {

    public UserDPO user;
    public DeviceDPO device;

    public void validate() {
        PreconditionUtils.checkRequired("user", user);
        PreconditionUtils.checkRequired("device", device);
        validateUser();
        device.validate();
    }

    private void validateUser() {
        PreconditionUtils.checkRequiredMobile("user.mobile", user.mobile);
        PreconditionUtils.checkRequired("user.password", user.password);
    }

}
