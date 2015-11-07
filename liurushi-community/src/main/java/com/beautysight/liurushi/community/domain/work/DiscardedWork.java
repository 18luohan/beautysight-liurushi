/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.domain.work;

import com.beautysight.liurushi.common.utils.Beans;
import org.mongodb.morphia.annotations.Entity;

/**
 * @author chenlong
 * @since 1.0
 */
@Entity(value = "discarded_works", noClassnameStored = true)
public class DiscardedWork extends Work {

    public DiscardedWork() {
    }

    public DiscardedWork(Work work) {
        Beans.copyProperties(work, this);
    }

    public Work transformToWork() {
        Work work = new Work();
        Beans.copyProperties(this, work);
        return work;
    }

}