/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.social.app;

import com.beautysight.liurushi.common.domain.Pageable;
import com.beautysight.liurushi.common.ex.IllegalDomainStateException;
import com.beautysight.liurushi.identityaccess.domain.repo.UserRepo;
import com.beautysight.liurushi.social.domain.follow.Follow;
import com.beautysight.liurushi.social.domain.follow.FollowDPO;
import com.beautysight.liurushi.social.domain.follow.FollowRepo;
import com.beautysight.liurushi.social.domain.follow.FollowService;
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

    public FollowersPresentation findFollowersInRange(String followingId, Pageable range) {
        List<FollowDPO> followers = followService.findFollowInRange(
                FollowRepo.QueryType.follower, followingId, range);
        return FollowersPresentation.from(followers);
    }

    public FollowingsPresentation findFollowingsInRange(String followerId, Pageable range) {
        List<FollowDPO> followings = followService.findFollowInRange(
                FollowRepo.QueryType.following, followerId, range);
        return FollowingsPresentation.from(followings);
    }

}
