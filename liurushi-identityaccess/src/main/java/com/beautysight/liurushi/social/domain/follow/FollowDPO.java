/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.social.domain.follow;

import com.beautysight.liurushi.common.app.DPO;
import com.beautysight.liurushi.identityaccess.domain.dpo.UserDPO;
import com.google.common.base.Preconditions;

/**
 * @author chenlong
 * @since 1.0
 */
public class FollowDPO extends DPO {

    public String id;
    public UserDPO follower;
    public UserDPO following;

    public static FollowDPO newFollowerWith(UserDPO user, Follow follow) {
        Preconditions.checkState(follow.followerId().equals(user.id), "following not match");
        FollowDPO instance = new FollowDPO();
        instance.id = follow.idAsStr();
        instance.follower = user;
        return instance;
    }

    public static FollowDPO newFolowingWith(UserDPO user, Follow follow) {
        Preconditions.checkState(follow.followingId().equals(user.id), "follower not match");
        FollowDPO instance = new FollowDPO();
        instance.id = follow.idAsStr();
        instance.following = user;
        return instance;
    }

}