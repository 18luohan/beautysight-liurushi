/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.app.presentation;

import com.beautysight.liurushi.common.app.ViewModel;
import com.beautysight.liurushi.community.app.WorkProfileVM;
import com.beautysight.liurushi.community.app.WorkProfileVMV10;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chenlong
 * @since 1.0
 */
public class WorkProfileList implements ViewModel {

    private List<? extends WorkProfileVM> workProfiles;

    public WorkProfileList(List<? extends WorkProfileVM> workProfiles) {
        this.workProfiles = workProfiles;
    }

    public static WorkProfileList toWorkProfileVMV10List(WorkProfileList srcList) {
        int size = CollectionUtils.isEmpty(srcList.workProfiles) ? 0 : srcList.workProfiles.size();
        List<WorkProfileVMV10> newList = new ArrayList<>(size);
        if (size > 0) {
            for (WorkProfileVM workProfileVM : srcList.workProfiles) {
                newList.add(new WorkProfileVMV10(workProfileVM));
            }
        }
        return new WorkProfileList(newList);
    }

}
