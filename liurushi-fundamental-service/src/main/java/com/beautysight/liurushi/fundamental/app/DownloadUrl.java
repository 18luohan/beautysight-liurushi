/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.fundamental.app;

import com.beautysight.liurushi.common.app.ViewModel;

/**
 * @author chenlong
 * @since 1.0
 */
public class DownloadUrl implements ViewModel {

    private String downloadUrl;

    private DownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public static DownloadUrl from(String downloadUrl) {
        return new DownloadUrl(downloadUrl);
    }

}
