/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.app.user;

import com.beautysight.liurushi.common.app.Command;
import com.beautysight.liurushi.common.ex.IllegalParamException;
import com.beautysight.liurushi.common.utils.PreconditionUtils;
import com.beautysight.liurushi.identityaccess.domain.user.pl.DevicePayload;
import com.beautysight.liurushi.identityaccess.domain.user.pl.UserPayload;
import com.beautysight.liurushi.identityaccess.common.UserErrorId;
import org.apache.commons.lang3.StringUtils;

/**
 * @author chenlong
 * @since 1.0
 */
public class SignUpCommand implements Command {

    public UserPayload user;
    public DevicePayload device;

    public void validate() {
        PreconditionUtils.checkRequired("user", user);
        PreconditionUtils.checkRequired("device", device);
        validateUser();
        device.validate();
    }

    private void validateUser() {
        // 如果user.origin为空或self，就表明用户是通过我们自己的app注册
        if (user.origin == null || user.origin.isSelf()) {
            PreconditionUtils.checkRequiredMobile("user.mobile", user.mobile);
            PreconditionUtils.checkRequired("user.password", user.password);
            PreconditionUtils.checkRequired("user.confirmPassword", user.confirmPassword);
            if (!user.password.equals(user.confirmPassword)) {
                throw new IllegalParamException(UserErrorId.password_confirmpwd_not_equal,
                        "user password not equal to confirmPassword");
            }
        } else {
            PreconditionUtils.checkRequired("user.unionId", user.unionId);
        }

        if (StringUtils.isNotBlank(user.email)) {
            PreconditionUtils.checkEmail("user.email", user.email);
        }

        // 用户头像可选
        if (user.avatar != null) {
            PreconditionUtils.checkRequired("user.avatar.id", user.avatar.id);
            PreconditionUtils.checkRequired("user.avatar.hash", user.avatar.hash);
        }
    }

}