/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.app.presentation;

import com.beautysight.liurushi.common.app.Presentation;
import com.beautysight.liurushi.identityaccess.domain.model.AccessToken;
import com.google.common.base.Preconditions;

/**
 * Here is Javadoc.
 * <p/>
 * Created by chenlong on 2015-05-08.
 *
 * @author chenlong
 * @since 1.0
 */
public abstract class AccessTokenPresentation implements Presentation {

    public static AccessTokenPresentation from(AccessToken accessToken) {
        Preconditions.checkState((accessToken.type() != null), "accessToken.type is null");
        if (accessToken.type() == AccessToken.Type.Basic) {
            return new BasicToken(accessToken.accessToken());
        } else if (accessToken.type() == AccessToken.Type.Bearer) {
            return new BearerToken(accessToken.accessToken(), accessToken.refreshToken());
        } else {
            throw new IllegalStateException("Not supported token type:" + accessToken.type());
        }
    }

    public static class BearerToken extends AccessTokenPresentation {
        private String bearerToken;
        private String refreshToken;

        public BearerToken(String bearerToken, String refreshToken) {
            this.bearerToken = bearerToken;
            this.refreshToken = refreshToken;
        }
    }

    public static class BasicToken extends AccessTokenPresentation {
        private String basicToken;

        private BasicToken(String token) {
            this.basicToken = token;
        }
    }
}
