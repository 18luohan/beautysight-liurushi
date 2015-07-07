/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.app.presentation;

import com.beautysight.liurushi.common.app.Presentation;

/**
 * @author chenlong
 * @since 1.0
 */
public class DownloadUrlPresentation implements Presentation {

    private String downloadUrl;

    private DownloadUrlPresentation(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public static DownloadUrlPresentation from(String downloadUrl) {
        return new DownloadUrlPresentation(downloadUrl);
    }

}
