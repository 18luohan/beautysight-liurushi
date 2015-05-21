/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.domain.service;

import com.beautysight.liurushi.identityaccess.domain.model.UploadOptions;

/**
 * Here is Javadoc.
 * <p/>
 * Created by chenlong on 2015-05-18.
 *
 * @author chenlong
 * @since 1.0
 */
public interface StorageAuthService {

    String getUploadToken(UploadOptions options);

    String getDownloadUrl(String key, int expiry, String instructions, String savedAsKey);

}
