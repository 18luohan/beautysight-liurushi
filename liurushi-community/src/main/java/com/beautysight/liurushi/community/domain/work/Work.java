/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.domain.work;

import com.beautysight.liurushi.common.domain.ValueObject;
import com.beautysight.liurushi.common.ex.IllegalParamException;
import org.mongodb.morphia.annotations.Entity;

import java.util.Date;

/**
 * 已发表的作品
 *
 * @author chenlong
 * @since 1.0
 */
@Entity(value = "works", noClassnameStored = true)
public class Work extends AbstractWork {

    private static final PresentPriority DEFAULT_PRESENT_PRIORITY = PresentPriority.raw;
    private static final long PRESENT_PRIORITY_WEIGHT = Double.valueOf(1E13).longValue();

    protected Date publishedAt;
    protected Stats stats;
    protected Integer presentPriority;
    protected Long ordering;

    public Work() {
    }

    public void initialise() {
        this.stats = new Stats();
        this.publishedAt = this.createdAt;
        this.setPresentPriorityAndCalcOrderingVal(DEFAULT_PRESENT_PRIORITY);
    }

    public Date publishedAt() {
        return this.publishedAt;
    }

    public Stats stats() {
        return this.stats;
    }

    public Integer presentPriority() {
        return this.presentPriority;
    }

    public Long ordering() {
        return this.ordering;
    }

    public void setPresentPriorityAndCalcOrderingVal(PresentPriority presentPriority) {
        this.setPresentPriority(presentPriority);
        this.calculateOrderingVal();
    }

    private void setPresentPriority(PresentPriority presentPriority) {
        this.presentPriority = presentPriority.val();
    }

    private void calculateOrderingVal() {
        this.ordering = this.presentPriority.intValue() * PRESENT_PRIORITY_WEIGHT
                + this.publishedAt.getTime();
    }

    public static class Stats extends ValueObject {

        private static final int DEFAULT_VAL = 0;

        private Integer viewTimes = DEFAULT_VAL;
        private Integer likeTimes = DEFAULT_VAL;
        private Integer favoriteTimes = DEFAULT_VAL;
        private Integer commentTimes = DEFAULT_VAL;

        public Integer getViewTimes() {
            return viewTimes;
        }

        public Integer getLikeTimes() {
            return likeTimes;
        }

        public Integer getFavoriteTimes() {
            return favoriteTimes;
        }

        public Integer getCommentTimes() {
            return commentTimes;
        }

    }

    public enum PresentPriority {
        selected(6), ordinary(1), raw(0), bad(-1);

        private int val;

        PresentPriority(int val) {
            this.val = val;
        }

        public int val() {
            return this.val;
        }

        public static PresentPriority of(String priority) {
            try {
                return PresentPriority.valueOf(priority);
            } catch (IllegalArgumentException e) {
                throw new IllegalParamException(e.getMessage());
            }
        }
    }

}