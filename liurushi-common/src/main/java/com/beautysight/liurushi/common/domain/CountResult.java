/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.common.domain;

/**
 * @author chenlong
 * @since 1.0
 */
public class CountResult extends ValueObject {

    private int count;

    public CountResult() {
    }

    public CountResult(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }

}