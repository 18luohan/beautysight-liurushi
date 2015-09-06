/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.domain.auth;

import com.beautysight.liurushi.common.app.ViewModel;

/**
 * @author chenlong
 * @since 1.0
 */
public class AccessTokenVM implements ViewModel {

    private String accessToken;
    private String refreshToken;

    private AccessTokenVM(AccessToken accessToken) {
        this.accessToken = accessToken.type() + " " + accessToken.accessToken();
        this.refreshToken = accessToken.refreshToken();
    }

    public static AccessTokenVM of(AccessToken accessToken) {
        return new AccessTokenVM(accessToken);
    }

}
