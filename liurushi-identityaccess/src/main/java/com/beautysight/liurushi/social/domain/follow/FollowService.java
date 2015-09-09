/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.social.domain.follow;

import com.beautysight.liurushi.common.domain.Range;
import com.beautysight.liurushi.identityaccess.domain.user.UserService;
import com.beautysight.liurushi.identityaccess.domain.user.UserView;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * @author chenlong
 * @since 1.0
 */
@Service
public class FollowService {

    @Autowired
    private FollowRepo followRepo;
    @Autowired
    private UserService userService;

    public List<UserInFollow> findFollowersInRange(String followingId, Range range, Optional<String> loginUserId) {
        return findUsersInFollowInRange(FollowRepo.QueryType.follower, followingId, range, loginUserId);
    }

    public List<UserInFollow> findFollowingsInRange(String followerId, Range range, Optional<String> loginUserId) {
        return findUsersInFollowInRange(FollowRepo.QueryType.following, followerId, range, loginUserId);
    }

    private List<UserInFollow> findUsersInFollowInRange(FollowRepo.QueryType queryType, String followerOrFollowingId, Range range, Optional<String> loginUserId) {
        List<UserInFollow> usersInFollow = followRepo.findUsersInFollowInRange(queryType, followerOrFollowingId, range);
        if (CollectionUtils.isEmpty(usersInFollow)) {
            return Collections.EMPTY_LIST;
        }

        Map<String, UserInFollow> userIdToUserInFollowMapping = new HashMap<>(usersInFollow.size());
        for (UserInFollow userInFollow : usersInFollow) {
            userIdToUserInFollowMapping.put(userInFollow.getUserId(), userInFollow);
        }

        List<String> userIdsInFollow = Lists.newArrayList(userIdToUserInFollowMapping.keySet());
        List<UserView.LiteAndStats> userViews = userService.getLiteUsersWithStats(userIdsInFollow);
        for (UserView.LiteAndStats userView : userViews) {
            userIdToUserInFollowMapping.get(userView.getId()).setUser(userView);
        }

        if (loginUserId.isPresent()) {
            if (queryType == FollowRepo.QueryType.following && loginUserId.get().equals(followerOrFollowingId)) {

                /*
                 * 如果是查找当前登录用户所关注的人，那么就无需计算当前登录用户是否已关注这些人
                 */
                for (UserInFollow userInFollow : userIdToUserInFollowMapping.values()) {
                    userInFollow.setIsFollowedByLoginUserToTrue();
                }

            } else {
                List<Follow> follows = followRepo.findFollowsBy(loginUserId.get(), userIdsInFollow);
                for (Follow follow : follows) {
                    userIdToUserInFollowMapping.get(follow.followingId()).setIsFollowedByLoginUserToTrue();
                }
            }
        }

        return usersInFollow;
    }

}
