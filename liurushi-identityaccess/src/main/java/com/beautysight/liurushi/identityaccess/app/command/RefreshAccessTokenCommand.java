/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.app.command;

import com.beautysight.liurushi.common.app.Command;
import com.beautysight.liurushi.common.utils.PreconditionUtils;

/**
 * @author chenlong
 * @since 1.0
 */
public class RefreshAccessTokenCommand implements Command {

    public String bearerToken;
    public String refreshToken;

    public void validate() {
        PreconditionUtils.checkRequired("bearerToken", bearerToken);
        PreconditionUtils.checkRequired("refreshToken", refreshToken);
    }

}