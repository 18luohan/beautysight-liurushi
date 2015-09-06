/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.app.user;

import com.beautysight.liurushi.common.app.ViewModel;
import com.beautysight.liurushi.identityaccess.domain.auth.AccessToken;
import com.beautysight.liurushi.identityaccess.domain.auth.AccessTokenVM;
import com.beautysight.liurushi.identityaccess.domain.user.UserView;

/**
 * @author chenlong
 * @since 1.0
 */
public class UserAndAccessTokenVM implements ViewModel {

    private UserView.Whole userProfile;
    private AccessTokenVM accessToken;

    public UserAndAccessTokenVM(UserView.Whole userProfile, AccessToken accessToken) {
        this.userProfile = userProfile;
        this.accessToken = AccessTokenVM.of(accessToken);
    }

}
