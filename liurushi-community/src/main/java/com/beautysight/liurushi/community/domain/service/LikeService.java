/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.domain.service;

import com.beautysight.liurushi.community.domain.model.like.Like;
import com.beautysight.liurushi.community.domain.model.like.LikeRepo;
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
public class LikeService {

    private static final Logger logger = LoggerFactory.getLogger(LikeService.class);

    @Autowired
    private LikeRepo likeRepo;

    /**
     * 赞一个作品
     */
    public void likeWork(String workId, String userId) {
        Optional<Like> theLike = likeRepo.getLikeBy(workId, userId);
        if (theLike.isPresent()) {
            logger.info("Like of work already exists, workId: {}, userId: {}", workId, userId);
            return;
        }

        Like like = new Like(workId, userId);
        likeRepo.save(like);
    }

    /**
     * 取消对一个作品的赞
     *
     * @param workId
     * @param userId
     * @return
     */
    public int cancelLikeOfWork(String workId, String userId) {
        int affected = likeRepo.deleteLikeBy(workId, userId);
        logger.info("Deleted like of work, workId: {}, userId: {}", workId, userId);

        if (affected != 1) {
            logger.error("Affected on cancel like of work: expected %s, actual %s", 1, affected);
        }

        return affected;
    }

    /**
     * 指定的用户是否赞了指定的作品
     *
     * @param workId
     * @param userId
     * @return
     */
    public boolean isWorkLikedByUser(String workId, String userId) {
        Optional<Like> theLike = likeRepo.getLikeBy(workId, userId);
        return theLike.isPresent();
    }

    public List<Like> findUserLikesOfWorks(String userId, List<String> workIds) {
        return likeRepo.findLikesBy(userId, workIds);
    }

}
