/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.fundamental.domain.storage;

import com.google.common.base.Optional;

/**
 * @author chenlong
 * @since 1.0
 */
public interface StorageService {

    String issueUploadToken(String key);

    String downloadUrl(String key);

    String imgDownloadUrl(String key, ImgThumbnailSpec thumbnailSpec);

    String imgDownloadUrl(String key, Optional<Integer> intThumbnailSpec);

    String imgDownloadUrl(String key, Integer deviceResolutionWidth, String restApiUri);

    FileMetadata zoomImageTo(int expectedWidth, String originImageKey, String zoomedImageKey);

    FileMetadata blurImageAccordingTo(int radius, int sigma, String originalKey);

}
