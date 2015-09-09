/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.app.presentation;

import com.beautysight.liurushi.common.app.ViewModel;
import com.beautysight.liurushi.community.app.WorkProfile;

import java.util.List;

/**
 * @author chenlong
 * @since 1.0
 */
public class WorkProfilesVM implements ViewModel {

    private List<WorkProfile> workProfiles;

    public WorkProfilesVM(List<WorkProfile> workProfiles) {
        this.workProfiles = workProfiles;
    }

}