/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.app.presentation;

import com.beautysight.liurushi.common.app.Presentation;

/**
 * @author chenlong
 * @since 1.0
 */
public class UserExistPresentation implements Presentation {

    private boolean exist;

    public UserExistPresentation(boolean exist) {
        this.exist = exist;
    }

}
