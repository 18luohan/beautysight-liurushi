/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.app.user;

import com.beautysight.liurushi.common.app.Command;
import com.beautysight.liurushi.common.utils.PreconditionUtils;

/**
 * @author chenlong
 * @since 1.0
 */
public class ResetPasswordCommand implements Command {

    public String mobile;
    public String password;

    public void validate() {
        PreconditionUtils.checkRequiredMobile("mobile", this.mobile);
        PreconditionUtils.checkRequired("password", this.password);
    }

}
