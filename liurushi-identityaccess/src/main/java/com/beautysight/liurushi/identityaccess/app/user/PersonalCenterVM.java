/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.app.user;

import com.beautysight.liurushi.common.app.ViewModel;
import com.beautysight.liurushi.identityaccess.domain.user.Gender;
import com.beautysight.liurushi.identityaccess.domain.user.User;
import com.beautysight.liurushi.identityaccess.domain.user.UserView;

/**
 * @author chenlong
 * @since 1.0
 */
public class PersonalCenterVM implements ViewModel {

    private String id;
    private String nickname;
    private User.Group group;
    private User.Origin origin;
    private Gender gender;
    private String mobile;
    private String email;
    private String maxAvatarUrl;
    private String headerPhotoUrl;
    private Boolean isFollowed;
    private User.Stats stats;

    public PersonalCenterVM(UserView.Whole wholeUser, Boolean isFollowedByCurrentUser) {
        this.id = wholeUser.getId();
        this.nickname = wholeUser.getNickname();
        this.group = wholeUser.getGroup();
        this.origin = wholeUser.getOrigin();
        this.gender = wholeUser.getGender();
        this.mobile = wholeUser.getMobile();
        this.email = wholeUser.getEmail();
        this.maxAvatarUrl = wholeUser.getMaxAvatarUrl();
        this.headerPhotoUrl = wholeUser.getHeaderPhotoUrl();
        this.isFollowed = isFollowedByCurrentUser;
        this.stats = wholeUser.getStats();
    }

}
