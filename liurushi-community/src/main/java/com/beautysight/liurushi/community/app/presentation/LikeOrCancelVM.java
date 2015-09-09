/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.app.presentation;

import com.beautysight.liurushi.common.app.ViewModel;

/**
 * @author chenlong
 * @since 1.0
 */
public class LikeOrCancelVM implements ViewModel {

    private Integer likeTimes;

    public LikeOrCancelVM(Integer likeTimes) {
        this.likeTimes = likeTimes;
    }

}
