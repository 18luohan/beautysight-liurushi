/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.app.command;

import com.beautysight.liurushi.common.app.Command;
import com.beautysight.liurushi.common.domain.Range;
import com.google.common.base.Optional;

/**
 * @author chenlong
 * @since 1.0
 */
public class WorkQueryInRangeCommand implements Command {

    public Range range;
    public Optional<String> loginUserId;
    public Optional<Integer> thumbnailSpec;

    public WorkQueryInRangeCommand(Range range, String loginUserId, Integer thumbnailSpec) {
        this(range, Optional.fromNullable(loginUserId), thumbnailSpec);
    }

    public WorkQueryInRangeCommand(Range range, Optional<String> loginUserId, Integer thumbnailSpec) {
        this.range = range;
        this.loginUserId = loginUserId;
        this.thumbnailSpec = Optional.fromNullable(thumbnailSpec);
    }

}