/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.domain.work;

import com.beautysight.liurushi.common.domain.ValueObject;

import java.util.List;

/**
 * 作品的一个部分
 *
 * @author chenlong
 * @since 1.0
 */
public abstract class WorkPart<T extends Control> extends ValueObject {

    public abstract List<T> controls();

}