/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.app.dpo;

import com.beautysight.liurushi.common.app.DPO;
import com.beautysight.liurushi.common.utils.PreconditionUtils;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author chenlong
 * @since 1.0
 */
public class ControlDPO extends DPO {

    public String sectionId;
    public Integer order;

    public ContentSectionDPO content;

    public void validate() {
        PreconditionUtils.checkRequired(format("%s.sectionId"), sectionId);
        PreconditionUtils.checkGreaterThanOrEqZero(format("%s.order"), order);
    }

    public static void validate(List<? extends ControlDPO> parts) {
        Assert.notEmpty(parts, "units must not be null or empty");
        int orderSum = 0;
        for (ControlDPO part : parts) {
            part.validate();
            orderSum += part.order;
        }

        if (orderSum != (parts.get(0).order + parts.get(parts.size()-1).order) * parts.size() / 2) {
            throw new IllegalArgumentException("order must be consecutive and the step must be 1.");
        }
    }

    public static void sortByOrderAsc(List<? extends ControlDPO> parts) {
        Collections.sort(parts, comparatorByOrder);
    }

    private static final Comparator<ControlDPO> comparatorByOrder = new Comparator<ControlDPO>() {
        @Override
        public int compare(ControlDPO o1, ControlDPO o2) {
            return (o1.order - o2.order);
        }
    };

}
