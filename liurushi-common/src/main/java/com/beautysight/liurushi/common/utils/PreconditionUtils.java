/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.common.utils;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;

/**
 * Here is Javadoc.
 * <p/>
 * Created by chenlong on 2015-05-08.
 *
 * @author chenlong
 * @since 1.0
 */
public class PreconditionUtils {

    public static void checkRequired(String field, String val) {
        Preconditions.checkArgument(StringUtils.isNotBlank(val), "%s required", field);
    }

    public static void checkRequired(String field, Object val) {
        Preconditions.checkArgument((val != null), "%s required", field);
    }

    public static void checkGreaterThanZero(String field, int val) {
        checkGreaterThan(0, field, val);
    }

    public static void checkGreaterThan(int bound, String field, int val) {
        Preconditions.checkArgument((val > bound), "%s must be greater than %s", field, bound);
    }

}
