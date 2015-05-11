/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.domain.model;

import com.beautysight.liurushi.common.domain.ValueObject;
import com.beautysight.liurushi.common.utils.PreconditionUtils;

/**
 * Here is Javadoc.
 * <p/>
 * Created by chenlong on 2015-05-08.
 *
 * @author chenlong
 * @since 1.0
 */
public class Resolution implements ValueObject {

    private int width;
    private int height;

    public void validate() {
        PreconditionUtils.checkGreaterThanZero("resolution.width", width);
        PreconditionUtils.checkGreaterThanZero("resolution.height", height);
    }

}
