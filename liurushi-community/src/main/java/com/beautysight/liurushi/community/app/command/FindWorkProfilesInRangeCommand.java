/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.app.command;

import com.beautysight.liurushi.common.app.Command;
import com.beautysight.liurushi.common.utils.PreconditionUtils;
import com.beautysight.liurushi.common.domain.OffsetDirection;

/**
 * @author chenlong
 * @since 1.0
 */
public class FindWorkProfilesInRangeCommand implements Command {

    public String referenceWork;
    public Integer offset;
    public OffsetDirection direction = OffsetDirection.both;

    public void validate() {
        PreconditionUtils.checkGreaterThanOrEqZero("offset", offset);
        PreconditionUtils.checkRequired("direction", direction);
    }

}