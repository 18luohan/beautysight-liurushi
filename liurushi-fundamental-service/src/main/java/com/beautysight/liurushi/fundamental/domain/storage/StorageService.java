/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.fundamental.domain.storage;

/**
 * @author chenlong
 * @since 1.0
 */
public interface StorageService {

    String issueUploadToken();

    String issueUploadToken(UploadOptions options);

    String downloadUrl(String key);

    FileMetadata zoomImageTo(int expectedWidth, String originImageKey, String zoomedImageKey);

    FileMetadata blurImageAccordingTo(int radius, int sigma, String originalKey);

}
