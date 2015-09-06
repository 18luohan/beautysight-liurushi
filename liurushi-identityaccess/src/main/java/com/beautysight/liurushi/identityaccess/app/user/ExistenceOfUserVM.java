/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.app.user;

import com.beautysight.liurushi.common.app.ViewModel;

/**
 * @author chenlong
 * @since 1.0
 */
public class ExistenceOfUserVM implements ViewModel {

    private boolean exist;

    public ExistenceOfUserVM(boolean exist) {
        this.exist = exist;
    }

}
