/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.social.domain.follow;

import com.beautysight.liurushi.common.domain.Range;
import com.beautysight.liurushi.identityaccess.domain.dpo.UserDPO;
import com.beautysight.liurushi.identityaccess.domain.service.UserService;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public List<FollowDPO> findFollowInRange(FollowRepo.QueryType type, String involvedUserId, Range range) {
        List<Follow> followList = followRepo.findFollowInRange(type, involvedUserId, range);
        List<FollowDPO> result = new ArrayList<>(followList.size());

        if (CollectionUtils.isEmpty(followList)) {
            return result;
        }

        Map<String, Follow> userIdToFollowMap = new HashMap<>(followList.size());
        for (Follow follow : followList) {
            userIdToFollowMap.put(followerOrFollowingId(type, follow), follow);
        }

        List<UserDPO> liteUsers = userService.getLiteUsersWithStats(Lists.newArrayList(userIdToFollowMap.keySet()));
        for (int i = 0; i < liteUsers.size(); i++) {
            UserDPO liteUser = liteUsers.get(i);
            result.add(followerOrFollowing(type, userIdToFollowMap.get(liteUser.id), liteUser));
        }

        return result;
    }

    private String followerOrFollowingId(FollowRepo.QueryType type, Follow follow) {
        if (type == FollowRepo.QueryType.follower) {
            return follow.followerId();
        } else if (type == FollowRepo.QueryType.following) {
            return follow.followingId();
        } else {
            throw new RuntimeException("Unexpected FollowType: " + type);
        }
    }

    private FollowDPO followerOrFollowing(FollowRepo.QueryType type, Follow follow, UserDPO involvedUser) {
        if (type == FollowRepo.QueryType.follower) {
            return FollowDPO.newFollowerWith(involvedUser, follow);
        } else if (type == FollowRepo.QueryType.following) {
            return FollowDPO.newFolowingWith(involvedUser, follow);
        } else {
            throw new RuntimeException("Unexpected FollowType: " + type);
        }
    }

}
