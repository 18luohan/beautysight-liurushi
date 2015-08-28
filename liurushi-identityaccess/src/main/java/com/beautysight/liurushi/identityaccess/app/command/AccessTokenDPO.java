/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.app.command;

import com.beautysight.liurushi.common.app.DPO;
import com.beautysight.liurushi.common.utils.PreconditionUtils;
import com.beautysight.liurushi.identityaccess.domain.model.AccessToken;

/**
 * @author chenlong
 * @since 1.0
 */
public class AccessTokenDPO extends DPO {

    public AccessToken.Type type;
    public String accessToken;

    public AccessTokenDPO(String type, String accessToken) {
        this.type = AccessToken.Type.valueOf(type);
        this.accessToken = accessToken;
    }

    public void validate() {
        PreconditionUtils.checkRequired("access token type", type);
        PreconditionUtils.checkRequired("access token", accessToken);
    }

}