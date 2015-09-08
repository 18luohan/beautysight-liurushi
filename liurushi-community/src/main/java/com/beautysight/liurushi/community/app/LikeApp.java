/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.app;

import com.beautysight.liurushi.community.app.command.LikeOrCancelCommand;
import com.beautysight.liurushi.community.domain.model.work.WorkRepo;
import com.beautysight.liurushi.community.domain.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author chenlong
 * @since 1.0
 */
@Service
public class LikeApp {

    @Autowired
    private LikeService likeService;
    @Autowired
    private WorkRepo workRepo;

    public void likeWork(LikeOrCancelCommand command) {
        likeService.likeWork(command.workId, command.userId);
        workRepo.increaseLikeTimesBy(1, command.workId);
    }

    public void cancelLikeOfWork(LikeOrCancelCommand command) {
        int affected = likeService.cancelLikeOfWork(command.workId, command.userId);
        workRepo.increaseLikeTimesBy(affected * (-1), command.workId);
    }

}
