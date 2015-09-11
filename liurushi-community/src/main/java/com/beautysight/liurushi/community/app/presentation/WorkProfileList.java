/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.app.presentation;

import com.beautysight.liurushi.common.app.ViewModel;
import com.beautysight.liurushi.community.app.WorkProfileVM;

import java.util.List;

/**
 * @author chenlong
 * @since 1.0
 */
public class WorkProfileList implements ViewModel {

    private List<WorkProfileVM> workProfiles;

    public WorkProfileList(List<WorkProfileVM> workProfiles) {
        this.workProfiles = workProfiles;
    }

}
