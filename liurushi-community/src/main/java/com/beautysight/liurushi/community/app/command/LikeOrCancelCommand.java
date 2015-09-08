/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.app.command;

import com.beautysight.liurushi.common.app.Command;
import com.beautysight.liurushi.common.utils.PreconditionUtils;

/**
 * @author chenlong
 * @since 1.0
 */
public class LikeOrCancelCommand implements Command {

    public String workId;
    public String userId;

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void validate() {
        PreconditionUtils.checkRequired("workId", workId);
        PreconditionUtils.checkRequired("userId", userId);
    }

}
