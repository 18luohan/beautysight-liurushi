/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.app.auth;

import com.beautysight.liurushi.common.app.Command;
import com.beautysight.liurushi.common.utils.PreconditionUtils;
import com.beautysight.liurushi.identityaccess.domain.auth.AccessToken;

/**
 * @author chenlong
 * @since 1.0
 */
public class AuthCommand implements Command {

    public AccessToken.Type type;
    public String accessToken;

    public AuthCommand(String type, String accessToken) {
        this(AccessToken.Type.valueOf(type), accessToken);
    }

    public AuthCommand(AccessToken.Type type, String accessToken) {
        this.type = type;
        this.accessToken = accessToken;
    }

    public void validate() {
        PreconditionUtils.checkRequired("accessToken.type", type);
        PreconditionUtils.checkRequired("accessToken.accessToken", accessToken);
    }

}
