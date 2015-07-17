/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.fundamental.app;

import com.beautysight.liurushi.common.app.PresentationModel;

import java.util.List;

/**
 * @author chenlong
 * @since 1.0
 */
public class UploadTokenPresentation implements PresentationModel {

    private String uploadToken;
    private List<String> uploadTokens;

    private UploadTokenPresentation(String uploadToken) {
        this.uploadToken = uploadToken;
    }

    private UploadTokenPresentation(List<String> uploadTokens) {
        this.uploadTokens = uploadTokens;
    }

    public static UploadTokenPresentation from(String uploadToken) {
        return new UploadTokenPresentation(uploadToken);
    }

    public static UploadTokenPresentation from(List<String> uploadTokens) {
        return new UploadTokenPresentation(uploadTokens);
    }

}