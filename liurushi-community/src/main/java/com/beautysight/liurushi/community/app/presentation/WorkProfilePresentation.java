/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.app.presentation;

import com.beautysight.liurushi.common.app.PresentationModel;
import com.beautysight.liurushi.community.app.WorkProfile;

import java.util.List;

/**
 * @author chenlong
 * @since 1.0
 */
public class WorkProfilePresentation implements PresentationModel {

    private List<WorkProfile> workProfiles;

    public WorkProfilePresentation(List<WorkProfile> workProfiles) {
        this.workProfiles = workProfiles;
    }

}
