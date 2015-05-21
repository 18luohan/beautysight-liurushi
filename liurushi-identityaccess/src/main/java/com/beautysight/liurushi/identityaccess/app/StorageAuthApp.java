/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.app;

import com.beautysight.liurushi.identityaccess.app.command.GetDownloadUrlCommand;
import com.beautysight.liurushi.identityaccess.app.command.GetUploadTokenCommand;
import com.beautysight.liurushi.identityaccess.app.presentation.DownloadUrlPresentation;
import com.beautysight.liurushi.identityaccess.app.presentation.UploadTokenPresentation;
import com.beautysight.liurushi.identityaccess.domain.service.StorageAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Here is Javadoc.
 * <p/>
 * Created by chenlong on 2015-05-18.
 *
 * @author chenlong
 * @since 1.0
 */
@Service
public class StorageAuthApp {

    @Autowired
    private StorageAuthService authService;

    public UploadTokenPresentation getUploadToken(GetUploadTokenCommand command) {
        return UploadTokenPresentation.from(authService.getUploadToken(command.toUploadOptions()));
    }

    public DownloadUrlPresentation getDownloadUrl(GetDownloadUrlCommand command) {
        command.validate();
        String url = authService.getDownloadUrl(command.key, command.expiry, command.instructions, command.savedAsKey);
        return DownloadUrlPresentation.from(url);
    }

}
