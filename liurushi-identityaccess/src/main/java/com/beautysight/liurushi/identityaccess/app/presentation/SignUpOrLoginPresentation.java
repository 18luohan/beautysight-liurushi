/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.app.presentation;

import com.beautysight.liurushi.common.app.PresentationModel;

/**
 * Here is Javadoc.
 * <p/>
 * Created by chenlong on 2015-06-03.
 *
 * @author chenlong
 * @since 1.0
 */
public class SignUpOrLoginPresentation implements PresentationModel {

    private UserProfilePresentation userProfile;
    private AccessTokenPresentation accessToken;

    public SignUpOrLoginPresentation(UserProfilePresentation userProfile, AccessTokenPresentation accessToken) {
        this.userProfile = userProfile;
        this.accessToken = accessToken;
    }

}
