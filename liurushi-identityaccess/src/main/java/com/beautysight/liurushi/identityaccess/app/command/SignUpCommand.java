/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.app.command;

import com.beautysight.liurushi.common.app.Command;
import com.beautysight.liurushi.common.ex.IllegalParamException;
import com.beautysight.liurushi.common.utils.PreconditionUtils;
import com.beautysight.liurushi.identityaccess.common.UserErrorId;
import org.apache.commons.lang3.StringUtils;

/**
 * Here is Javadoc.
 * <p/>
 * Created by chenlong on 2015-05-08.
 *
 * @author chenlong
 * @since 1.0
 */
public class SignUpCommand implements Command {

    public UserDPO user;
    public DeviceDPO device;

    public void validate() {
        PreconditionUtils.checkRequired("user", user);
        PreconditionUtils.checkRequired("device", device);
        validateUser();
        device.validate();
    }

    private void validateUser() {
        // nickname 可选
        PreconditionUtils.checkRequiredMobile("user.mobile", user.mobile);
        PreconditionUtils.checkRequired("user.password", user.password);
        PreconditionUtils.checkRequired("user.confirmPassword", user.confirmPassword);

        if (!user.password.equals(user.confirmPassword)) {
            throw new IllegalParamException(UserErrorId.password_confirmpwd_not_equal,
                    "user password not equal to confirmPassword");
        }

        if (StringUtils.isNotBlank(user.email)) {
            PreconditionUtils.checkEmail("user.email", user.email);
        }

        // 用户头像可选
        if (user.avatar != null) {
            user.avatar.validate();
        }
    }

}