/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.app.dpo;

import com.beautysight.liurushi.common.app.Payload;
import com.beautysight.liurushi.common.utils.PreconditionUtils;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author chenlong
 * @since 1.0
 */
public class ControlPayload extends Payload {

    public String sectionId;
    public Integer order;

    public ContentSectionPayload content;

    public void validate() {
        PreconditionUtils.checkRequired(format("%s.sectionId"), sectionId);
        PreconditionUtils.checkGreaterThanOrEqZero(format("%s.order"), order);
    }

    public static void validate(List<? extends ControlPayload> parts) {
        Assert.notEmpty(parts, "units must not be null or empty");
        int orderSum = 0;
        for (ControlPayload part : parts) {
            part.validate();
            orderSum += part.order;
        }

        if (orderSum != (parts.get(0).order + parts.get(parts.size()-1).order) * parts.size() / 2) {
            throw new IllegalArgumentException("order must be consecutive and the step must be 1.");
        }
    }

    public static void sortByOrderAsc(List<? extends ControlPayload> parts) {
        Collections.sort(parts, comparatorByOrder);
    }

    private static final Comparator<ControlPayload> comparatorByOrder = new Comparator<ControlPayload>() {
        @Override
        public int compare(ControlPayload o1, ControlPayload o2) {
            return (o1.order - o2.order);
        }
    };

}
