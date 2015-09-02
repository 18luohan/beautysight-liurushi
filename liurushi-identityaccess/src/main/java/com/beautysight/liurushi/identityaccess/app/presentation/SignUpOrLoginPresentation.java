/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.app.presentation;

import com.beautysight.liurushi.common.app.PresentationModel;
import com.beautysight.liurushi.identityaccess.domain.dpo.UserDPO;

/**
 * Here is Javadoc.
 * <p/>
 * Created by chenlong on 2015-06-03.
 *
 * @author chenlong
 * @since 1.0
 */
public class SignUpOrLoginPresentation implements PresentationModel {

    private UserDPO userProfile;
    private AccessTokenPresentation accessToken;

    public SignUpOrLoginPresentation(UserDPO userProfile, AccessTokenPresentation accessToken) {
        this.userProfile = userProfile;
        this.accessToken = accessToken;
    }

}
