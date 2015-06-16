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

    String getUploadToken(UploadOptions options);

    String getDownloadUrl(String key, int expiry, String instructions, String savedAsKey);

    ResourceInStorage zoomImageAccordingTo(int expectedWidth, String originalKey);

}
