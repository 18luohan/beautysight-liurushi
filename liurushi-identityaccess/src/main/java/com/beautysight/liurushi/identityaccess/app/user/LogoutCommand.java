/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.app.user;

import com.beautysight.liurushi.common.app.Command;
import com.beautysight.liurushi.identityaccess.domain.auth.AccessToken;

/**
 * @author chenlong
 * @since 1.0
 */
public class LogoutCommand implements Command {

    public AccessToken.Type type;
    public String accessToken;

    public LogoutCommand(String type, String accessToken) {
        this.type = AccessToken.Type.valueOf(type);
        this.accessToken = accessToken;
    }

}
