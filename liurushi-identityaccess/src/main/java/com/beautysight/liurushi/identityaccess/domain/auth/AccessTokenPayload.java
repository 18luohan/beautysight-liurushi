/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.domain.auth;

import com.beautysight.liurushi.common.app.Payload;
import com.beautysight.liurushi.common.utils.PreconditionUtils;

/**
 * @author chenlong
 * @since 1.0
 */
public class AccessTokenPayload extends Payload {

    public AccessToken.Type type;
    public String accessToken;

    public AccessTokenPayload(String type, String accessToken) {
        this.type = AccessToken.Type.valueOf(type);
        this.accessToken = accessToken;
    }

    public void validate() {
        PreconditionUtils.checkRequired("access token type", type);
        PreconditionUtils.checkRequired("access token", accessToken);
    }

    @Override
    public String toString() {
        return new StringBuilder("{type:").append(this.type)
                .append(", accessToken:").append(this.accessToken)
                .append("}").toString();
    }

}