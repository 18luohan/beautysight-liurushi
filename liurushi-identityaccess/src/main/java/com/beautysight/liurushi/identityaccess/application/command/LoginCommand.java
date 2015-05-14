/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.application.command;

import com.beautysight.liurushi.common.application.Command;
import com.beautysight.liurushi.common.utils.PreconditionUtils;

/**
 * Here is Javadoc.
 * <p/>
 * Created by chenlong on 2015-05-08.
 *
 * @author chenlong
 * @since 1.0
 */
public class LoginCommand implements Command {

    public UserDTO user;
    public DeviceDTO device;

    public void validate() {
        PreconditionUtils.checkRequired("user", user);
        PreconditionUtils.checkRequired("device", device);
        validateUser();
        device.validate();
    }

    private void validateUser() {
        PreconditionUtils.checkRequired("user.mobilePhone", user.mobilePhone);
        PreconditionUtils.checkRequired("user.password", user.password);
    }

}
