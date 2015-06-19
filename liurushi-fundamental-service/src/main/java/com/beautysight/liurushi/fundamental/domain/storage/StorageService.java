/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.fundamental.domain.storage;


/**
 * Here is Javadoc.
 * <p/>
 * Created by chenlong on 2015-05-18.
 *
 * @author chenlong
 * @since 1.0
 */
public interface StorageService {

    String issueUploadToken(UploadOptions options);

    String issueDownloadUrl(String key);

    String issueDownloadUrl(String key, int expiry, String instructions, String savedAsKey);

    ResourceInStorage zoomImageTo(int expectedWidth, String imageKey);

    ResourceInStorage blurImageAccordingTo(int radius, int sigma, String originalKey);

}
