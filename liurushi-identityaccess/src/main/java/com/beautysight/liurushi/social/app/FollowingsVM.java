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
public class FollowingsVM extends FollowListVM {

    private List<UserInFollowVM> followings;

    public FollowingsVM(List<UserInFollow> usersInFollow) {
        this.followings = from(usersInFollow);
    }

}
