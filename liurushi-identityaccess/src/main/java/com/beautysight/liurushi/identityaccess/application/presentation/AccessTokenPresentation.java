/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.application.presentation;

import com.beautysight.liurushi.common.application.Presentation;
import com.beautysight.liurushi.identityaccess.domain.model.AccessToken;

/**
 * Here is Javadoc.
 * <p/>
 * Created by chenlong on 2015-05-08.
 *
 * @author chenlong
 * @since 1.0
 */
public class AccessTokenPresentation implements Presentation {

    private String accessToken;
    private String refreshToken;

    public AccessTokenPresentation(AccessToken accessToken) {
        this.accessToken = accessToken.accessToken();
        this.refreshToken = accessToken.refreshToken();
    }

    public static AccessTokenPresentation from(AccessToken accessToken) {
        return new AccessTokenPresentation(accessToken);
    }

}
