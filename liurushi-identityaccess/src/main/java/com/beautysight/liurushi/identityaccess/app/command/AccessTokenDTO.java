/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.app.command;

import com.beautysight.liurushi.common.app.DTO;
import com.beautysight.liurushi.common.utils.PreconditionUtils;
import com.beautysight.liurushi.identityaccess.domain.model.AccessToken;

/**
 * Here is Javadoc.
 * <p/>
 * Created by chenlong on 2015-05-28.
 *
 * @author chenlong
 * @since 1.0
 */
public class AccessTokenDTO extends DTO {

    public String accessToken;
    public AccessToken.Type type;

    public AccessTokenDTO(AccessToken.Type type, String accessToken) {
        this.type = type;
        this.accessToken = accessToken;
    }

    public void validate() {
        PreconditionUtils.checkRequired("accessToken.type", type);
        PreconditionUtils.checkRequired("accessToken.accessToken", accessToken);
    }

}
