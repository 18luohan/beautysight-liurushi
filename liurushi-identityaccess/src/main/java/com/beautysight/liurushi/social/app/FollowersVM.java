/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.social.app;

import com.beautysight.liurushi.social.domain.follow.UserInFollow;

import java.util.List;

/**
 * @author chenlong
 * @since 1.0
 */
public class FollowersVM extends FollowListVM {

    private List<UserInFollowVM> followers;

    public FollowersVM(List<UserInFollow> usersInFollow) {
        this.followers = from(usersInFollow);
    }

}


