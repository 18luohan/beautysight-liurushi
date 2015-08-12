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

    String issueDownloadUrl(String key);

    String issueDownloadUrlWithFileOps(final String key, final int expiry, final String instructions, final String savedAsKey);

    FileMetadata zoomImageTo(int expectedWidth, String originImageKey, String zoomedImageKey);

    FileMetadata blurImageAccordingTo(int radius, int sigma, String originalKey);

}
