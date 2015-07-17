/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.interfaces.identityaccess.facade.dto;

import com.beautysight.liurushi.common.utils.PreconditionUtils;

/**
 * @author chenlong
 * @since 1.0
 */
public class AccessTokenDTO implements DTO {

    public Type type;
    public String accessToken;

    public AccessTokenDTO(Type type, String accessToken) {
        this.type = type;
        this.accessToken = accessToken;
    }

    public void validate() {
        PreconditionUtils.checkRequired("accessToken.type", type);
        PreconditionUtils.checkRequired("accessToken.accessToken", accessToken);
    }

    public enum Type {
        Basic, Bearer
    }

}
