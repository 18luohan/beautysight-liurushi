/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.social.app;

import com.beautysight.liurushi.common.app.PresentationModel;
import com.beautysight.liurushi.social.domain.follow.FollowDPO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chenlong
 * @since 1.0
 */
public class FollowingsPresentation implements PresentationModel {

    private List<UserInFollow> followings;

    public static FollowingsPresentation from(List<FollowDPO> followList) {
        List<UserInFollow> followings = new ArrayList<>(followList.size());
        for (FollowDPO followDPO : followList) {
            followings.add(new UserInFollow(followDPO.id, followDPO.following));
        }

        FollowingsPresentation result = new FollowingsPresentation();
        result.followings = followings;
        return result;
    }

}
