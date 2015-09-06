/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.social.app;

import com.beautysight.liurushi.common.domain.Range;
import com.beautysight.liurushi.common.ex.IllegalDomainStateException;
import com.beautysight.liurushi.identityaccess.domain.user.UserRepo;
import com.beautysight.liurushi.social.domain.follow.Follow;
import com.beautysight.liurushi.social.domain.follow.FollowRepo;
import com.beautysight.liurushi.social.domain.follow.FollowService;
import com.beautysight.liurushi.social.domain.follow.UserInFollow;
import com.google.common.base.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author chenlong
 * @since 1.0
 */
@Service
public class FollowApp {

    private static final Logger logger = LoggerFactory.getLogger(FollowApp.class);

    @Autowired
    private FollowRepo followRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private FollowService followService;

    public void follow(FollowOrNotCommand command) {
        Optional<Follow> theFollow = followRepo.getBy(command.followerId, command.followingId);
        if (theFollow.isPresent()) {
            logger.info("Follow exists: {} -> {}", command.followerId, command.followingId);
            return;
        }
        followRepo.save(new Follow(command.followerId, command.followingId));
        logger.info("Added follow: {} -> {}", command.followerId, command.followingId);

        userRepo.increaseFollowingsNumBy(1, command.followerId);
        userRepo.increaseFollowersNumBy(1, command.followingId);
    }

    public void unfollow(FollowOrNotCommand command) {
        int affected = followRepo.deleteBy(command.followerId, command.followingId);
        logger.info("Deleted follow: {} -> {}, affected: {}",
                command.followerId, command.followingId, affected);

        if (affected != 1) {
            throw new IllegalDomainStateException(
                    "Affected on unfollow: expected %s, actual %s", 1, affected);
        }

        userRepo.increaseFollowingsNumBy(-1, command.followerId);
        userRepo.increaseFollowersNumBy(-1, command.followingId);
    }

    public FollowersVM findFollowersInRange(String followingId, Range range, Optional<String> loginUserId) {
        List<UserInFollow> followers = followService.findFollowersInRange(followingId, range, loginUserId);
        return new FollowersVM(followers);
    }

    public FollowingsVM findFollowingsInRange(String followerId, Range range, Optional<String> loginUserId) {
        List<UserInFollow> followings = followService.findFollowingsInRange(followerId, range, loginUserId);
        return new FollowingsVM(followings);
    }

}
