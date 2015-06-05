/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.fundamental.storage.domain;

/**
 * Here is Javadoc.
 * <p/>
 * Created by chenlong on 2015-05-15.
 *
 * @author chenlong
 * @since 1.0
 */
public class UploadResult {
    private boolean successful = true;
    private String errorMsg;

    public String hash;
    public String key;
    public int imageWidth;
    public int imageHeight;

    public String requestId;

    public static UploadResult failed(String errorMsg) {
        UploadResult result = new UploadResult();
        result.successful = false;
        result.errorMsg = errorMsg;
        return result;
    }

    public boolean isSuccessful() {
        return this.successful;
    }
}
