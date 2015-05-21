/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.app.presentation;

import com.beautysight.liurushi.common.app.Presentation;

/**
 * Here is Javadoc.
 * <p/>
 * Created by chenlong on 2015-05-18.
 *
 * @author chenlong
 * @since 1.0
 */
public class UploadTokenPresentation implements Presentation {
    private String uploadToken;

    private UploadTokenPresentation(String uploadToken) {
        this.uploadToken = uploadToken;
    }

    public static UploadTokenPresentation from(String uploadToken) {
        return new UploadTokenPresentation(uploadToken);
    }
}
