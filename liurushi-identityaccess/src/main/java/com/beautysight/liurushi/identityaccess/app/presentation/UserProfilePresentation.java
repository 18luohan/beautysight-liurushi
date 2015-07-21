/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.app.presentation;

import com.beautysight.liurushi.common.app.PresentationModel;
import com.beautysight.liurushi.common.utils.Beans;
import com.beautysight.liurushi.identityaccess.domain.model.Gender;
import com.beautysight.liurushi.identityaccess.domain.model.User;

/**
 * @author chenlong
 * @since 1.0
 */
public class UserProfilePresentation implements PresentationModel {

    private String id;
    private String nickname;
    private Gender gender;
    private String mobile;
    private String email;
    private String originalAvatarUrl;
    private String maxAvatarUrl;

    public static UserProfilePresentation from(User.UserProfile userProfile, String originalAvatarUrl, String maxAvatarUrl) {
        UserProfilePresentation target = new UserProfilePresentation();
        Beans.copyProperties(userProfile, target);
        target.id = userProfile.id().toString();
        target.originalAvatarUrl = originalAvatarUrl;
        target.maxAvatarUrl = maxAvatarUrl;
        return target;
    }

}