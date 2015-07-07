/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.app.command;

import com.beautysight.liurushi.common.app.DTO;
import com.beautysight.liurushi.common.utils.PreconditionUtils;
import com.beautysight.liurushi.identityaccess.domain.model.AccessToken;

/**
 * @author chenlong
 * @since 1.0
 */
public class AccessTokenDTO extends DTO {

    public AccessToken.Type type;
    public String accessToken;

    public AccessTokenDTO(AccessToken.Type type, String accessToken) {
        this.type = type;
        this.accessToken = accessToken;
    }

    public void validate() {
        PreconditionUtils.checkRequired("accessToken.type", type);
        PreconditionUtils.checkRequired("accessToken.accessToken", accessToken);
    }

}
