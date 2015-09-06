/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.app.auth;

import com.beautysight.liurushi.common.app.Command;
import com.beautysight.liurushi.common.utils.PreconditionUtils;

/**
 * @author chenlong
 * @since 1.0
 */
public class RefreshAccessTokenCommand implements Command {

    public String accessToken;
    public String refreshToken;

    public void validate() {
        PreconditionUtils.checkRequired("accessToken", accessToken);
        PreconditionUtils.checkRequired("refreshToken", refreshToken);
    }

}