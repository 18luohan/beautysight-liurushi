/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.common.domain;

import com.beautysight.liurushi.common.shared.DomainModel;

import java.io.Serializable;

/**
 * A tag interface that represents value object.
 * @author chenlong
 * @since 1.0
 */
public abstract class ValueObject extends DomainModel implements JsonAnyFieldVisible, Serializable {
}
