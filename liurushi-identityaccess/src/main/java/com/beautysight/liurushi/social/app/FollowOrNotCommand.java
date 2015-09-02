/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.social.app;

import com.beautysight.liurushi.common.app.Command;
import com.beautysight.liurushi.common.utils.PreconditionUtils;

/**
 * @author chenlong
 * @since 1.0
 */
public class FollowOrNotCommand implements Command {

    public String followerId;
    public String followingId;

    public void validate() {
        PreconditionUtils.checkRequired("followerId", followerId);
        PreconditionUtils.checkRequired("followingId", followingId);
    }

}
