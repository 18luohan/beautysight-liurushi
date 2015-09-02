/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.app.command;

import com.beautysight.liurushi.common.domain.OffsetDirection;
import com.beautysight.liurushi.common.domain.Range;

/**
 * @author chenlong
 * @since 1.0
 */
public class AuthorWorksRange extends Range {

    private String authorId;

    public AuthorWorksRange(String authorId, String referencePoint, Integer offset, OffsetDirection direction) {
        super(referencePoint, offset, direction);
        this.authorId = authorId;
    }

    public String authorId() {
        return this.authorId;
    }

}
