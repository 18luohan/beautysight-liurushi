/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.social.domain.follow;

import com.beautysight.liurushi.common.domain.ValueObject;
import com.beautysight.liurushi.identityaccess.domain.user.UserView;

/**
 * @author chenlong
 * @since 1.0
 */
public class UserInFollow extends ValueObject {

    private String followId;
    private String userId;
    private UserView.LiteAndStats user;
    private Boolean isFollowedByLoginUser = Boolean.FALSE;

    public UserInFollow(String followId, String userId) {
        this.followId = followId;
        this.userId = userId;
    }

    public String getFollowId() {
        return followId;
    }

    public String getUserId() {
        return userId;
    }

    public UserView.LiteAndStats getUser() {
        return user;
    }

    public Boolean isFollowedByLoginUser() {
        return this.isFollowedByLoginUser;
    }

    public void setUser(UserView.LiteAndStats user) {
        this.user = user;
    }

    public void setIsFollowedByLoginUserToTrue() {
        this.isFollowedByLoginUser = Boolean.TRUE;
    }

}
