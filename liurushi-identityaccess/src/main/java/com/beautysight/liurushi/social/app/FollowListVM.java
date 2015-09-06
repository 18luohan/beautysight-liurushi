/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.social.app;

import com.beautysight.liurushi.common.app.ViewModel;
import com.beautysight.liurushi.social.domain.follow.UserInFollow;

import java.util.ArrayList;
import java.util.List;

/**
 * 关注列表。这里的 list 不是指计算机数据结构中的list，而是指实际业务中同一类业务实体的排列而已。
 *
 * @author chenlong
 * @since 1.0
 */
public abstract class FollowListVM implements ViewModel {

    protected static List<UserInFollowVM> from(List<UserInFollow> usersInFollow) {
        List<UserInFollowVM> result = new ArrayList<>(usersInFollow.size());
        for (UserInFollow userInFollow : usersInFollow) {
            result.add(new UserInFollowVM(userInFollow));
        }
        return result;
    }

    public static class UserInFollowVM implements ViewModel {

        private String id;
        private String nickname;
        private String maxAvatarUrl;
        private Integer followersNum;
        private String followId;
        private Boolean isFollowed;

        public UserInFollowVM(UserInFollow userInFollow) {
            this.id = userInFollow.getUser().getId();
            this.nickname = userInFollow.getUser().getNickname();
            this.maxAvatarUrl = userInFollow.getUser().getMaxAvatarUrl();
            this.followersNum = userInFollow.getUser().getStats().getFollowersNum();
            this.isFollowed = userInFollow.isFollowedByLoginUser();
            this.followId = userInFollow.getFollowId();
        }

    }

}
