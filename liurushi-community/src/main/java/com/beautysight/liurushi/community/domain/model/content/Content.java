/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.domain.model.content;

import com.beautysight.liurushi.common.domain.AbstractEntity;

import java.util.List;

/**
 * Represents the content that produced by typical users, professional and occupational staff,
 * which we call UGC, PGC and OGC respectively.
 *
 * @author chenlong
 * @since 1.0
 */
public abstract class Content<T extends ComponentPart> extends AbstractEntity {

    public abstract List<T> componentParts();

}
