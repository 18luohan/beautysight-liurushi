/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.app.command;

import com.beautysight.liurushi.common.app.Command;
import com.beautysight.liurushi.common.ex.ParamValidationException;
import com.beautysight.liurushi.common.utils.PreconditionUtils;
import com.beautysight.liurushi.identityaccess.common.UserErrorId;

/**
 * Here is Javadoc.
 * <p/>
 * Created by chenlong on 2015-05-08.
 *
 * @author chenlong
 * @since 1.0
 */
public class SignUpCommand implements Command {

    public UserDTO user;
    public DeviceDTO device;

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
        PreconditionUtils.checkRequired("user.avatar", user.avatar);
        user.avatar.validate();
        if (!user.password.equals(user.confirmPassword)) {
            throw new ParamValidationException(UserErrorId.password_confirmpwd_not_equal,
                    "user password not equal to confirmPassword");
        }
    }

}
