/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.social.domain.follow;

import com.beautysight.liurushi.common.app.Payload;
import com.beautysight.liurushi.identityaccess.domain.user.UserView;
import com.google.common.base.Preconditions;

/**
 * @author chenlong
 * @since 1.0
 */
public class FollowPayload extends Payload {

    public String id;
    public UserView.LiteAndStats follower;
    public UserView.LiteAndStats following;

    public static FollowPayload newFollowerWith(UserView.LiteAndStats userView, Follow follow) {
        Preconditions.checkState(follow.followerId().equals(userView.getId()), "following not match");
        FollowPayload instance = new FollowPayload();
        instance.id = follow.idAsStr();
        instance.follower = userView;
        return instance;
    }

    public static FollowPayload newFolowingWith(UserView.LiteAndStats userView, Follow follow) {
        Preconditions.checkState(follow.followingId().equals(userView.getId()), "follower not match");
        FollowPayload instance = new FollowPayload();
        instance.id = follow.idAsStr();
        instance.following = userView;
        return instance;
    }

}