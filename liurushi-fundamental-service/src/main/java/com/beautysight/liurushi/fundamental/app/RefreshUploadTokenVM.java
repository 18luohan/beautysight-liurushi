/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.fundamental.app;

import com.beautysight.liurushi.common.app.ViewModel;

import java.util.Map;

/**
 * @author chenlong
 * @since 1.0
 */
public class RefreshUploadTokenVM implements ViewModel {

    private Map<String, String> uploadTokensMap;

    public RefreshUploadTokenVM(Map<String, String> fileIdToUploadTokenMap) {
        this.uploadTokensMap = fileIdToUploadTokenMap;
    }

}
