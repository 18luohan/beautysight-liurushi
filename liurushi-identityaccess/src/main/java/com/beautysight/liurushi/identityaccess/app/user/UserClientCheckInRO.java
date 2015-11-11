/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.app.user;

/**
 * @author chenlong
 * @since 1.0
 */

import com.beautysight.liurushi.identityaccess.domain.user.ClientApp;
import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class UserClientCheckInRO {

    private ClientApp latestApp;
    private ClientApp installedApp;

    public UserClientCheckInRO(ClientApp latestApp, ClientApp installedApp) {
        this.latestApp = latestApp;
        this.installedApp = installedApp;
    }

}
