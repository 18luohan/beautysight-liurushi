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
public class FollowersPresentation implements PresentationModel {

    private List<UserInFollow> followers;

    public static FollowersPresentation from(List<FollowDPO> followList) {
        List<UserInFollow> followers = new ArrayList<>(followList.size());
        for (FollowDPO followDPO : followList) {
            followers.add(new UserInFollow(followDPO.id, followDPO.follower));
        }

        FollowersPresentation result = new FollowersPresentation();
        result.followers = followers;
        return result;
    }

}


