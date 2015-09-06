/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.fundamental.app;

import com.beautysight.liurushi.common.app.ViewModel;

/**
 * @author chenlong
 * @since 1.0
 */
public class UploadTokenPresentation implements ViewModel {

    private String uploadToken;

    private UploadTokenPresentation(String uploadToken) {
        this.uploadToken = uploadToken;
    }

    public static UploadTokenPresentation from(String uploadToken) {
        return new UploadTokenPresentation(uploadToken);
    }

}