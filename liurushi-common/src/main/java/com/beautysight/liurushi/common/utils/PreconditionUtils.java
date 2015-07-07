/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.common.utils;

import com.beautysight.liurushi.common.ex.IllegalParamException;
import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author chenlong
 * @since 1.0
 */
public class PreconditionUtils {

    public static void checkRequired(String field, String val) {
        if (StringUtils.isBlank(val)) {
            throw new IllegalParamException("%s required", field);
        }
    }

    public static void checkRequired(String field, List<?> val) {
        if (CollectionUtils.isEmpty(val)) {
            throw new IllegalParamException("%s must not be null or empty", field);
        }
    }

    public static void checkRequired(String field, Object val) {
        if (val == null) {
            throw new IllegalParamException("%s required", field);
        }
    }

    public static void checkGreaterThanZero(String field, int val) {
        checkGreaterThan(0, field, val);
    }

    public static void checkGreaterThan(int bound, String field, int val) {
        if (val <= bound) {
            throw new IllegalParamException("%s must be greater than %s", field, bound);
        }
    }

    public static void checkGreaterThanOrEqZero(String field, int val) {
        checkGreaterThanOrEq(0, field, val);
    }

    public static void checkGreaterThanOrEq(int bound, String field, int val) {
        if (val < bound) {
            throw new IllegalParamException("%s must be greater than or eq %s", field, bound);
        }
    }

    public static void checkRequiredMobile(String field, String val) {
        checkRequired(field, val);
        if (!Regexp.isMobile(val)) {
            throw new IllegalParamException("%s illegal", field);
        }
    }

    public static void checkEmail(String field, String val) {
        Preconditions.checkArgument(StringUtils.isNotBlank(val),
                "given val of %s must not be blank", field);
        if (!Regexp.isEmail(val)) {
            throw new IllegalParamException("%s illegal", field);
        }
    }

}
