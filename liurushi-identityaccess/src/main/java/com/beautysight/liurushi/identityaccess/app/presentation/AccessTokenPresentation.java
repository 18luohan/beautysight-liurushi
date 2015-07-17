/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.app.presentation;

import com.beautysight.liurushi.common.app.PresentationModel;
import com.beautysight.liurushi.identityaccess.domain.model.AccessToken;

/**
 * @author chenlong
 * @since 1.0
 */
public class AccessTokenPresentation implements PresentationModel {

    private String accessToken;
    private String refreshToken;

    private AccessTokenPresentation(AccessToken accessToken) {
        this.accessToken = accessToken.type() + " " + accessToken.accessToken();
        this.refreshToken = accessToken.refreshToken();
    }

    public static AccessTokenPresentation from(AccessToken accessToken) {
        return new AccessTokenPresentation(accessToken);
    }

}
