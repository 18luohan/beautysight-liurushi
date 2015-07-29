/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.app.presentation;

import com.beautysight.liurushi.common.app.PresentationModel;
import com.beautysight.liurushi.common.utils.Beans;
import com.beautysight.liurushi.identityaccess.domain.model.Gender;
import com.beautysight.liurushi.identityaccess.domain.model.UserProfile;

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
    private String group;

    public static UserProfilePresentation from(UserProfile userProfile, String originalAvatarUrl, String maxAvatarUrl) {
        UserProfilePresentation target = new UserProfilePresentation();
        Beans.copyProperties(userProfile, target);
        target.id = userProfile.id().toString();
        target.originalAvatarUrl = originalAvatarUrl;
        target.maxAvatarUrl = maxAvatarUrl;
        target.group = userProfile.group().toString();
        return target;
    }

    public String getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }

    public Gender getGender() {
        return gender;
    }

    public String getMobile() {
        return mobile;
    }

    public String getEmail() {
        return email;
    }

    public String getOriginalAvatarUrl() {
        return originalAvatarUrl;
    }

    public String getMaxAvatarUrl() {
        return maxAvatarUrl;
    }

    public String getGroup() {
        return group;
    }
}