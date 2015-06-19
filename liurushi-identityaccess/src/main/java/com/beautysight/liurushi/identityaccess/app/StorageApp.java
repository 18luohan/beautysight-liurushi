/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.app;

import com.beautysight.liurushi.fundamental.domain.storage.StorageService;
import com.beautysight.liurushi.identityaccess.app.command.IssueDownloadUrlCommand;
import com.beautysight.liurushi.identityaccess.app.command.IssueUploadTokenCommand;
import com.beautysight.liurushi.identityaccess.app.presentation.DownloadUrlPresentation;
import com.beautysight.liurushi.identityaccess.app.presentation.UploadTokenPresentation;
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
public class StorageApp {

    @Autowired
    private StorageService storageService;

    public UploadTokenPresentation issueUploadToken(IssueUploadTokenCommand command) {
        return UploadTokenPresentation.from(storageService.issueUploadToken(command.toUploadOptions()));
    }

    public DownloadUrlPresentation issueDownloadUrl(IssueDownloadUrlCommand command) {
        command.validate();
        String url = storageService.issueDownloadUrl(command.key, command.expiry, command.instructions, command.savedAsKey);
        return DownloadUrlPresentation.from(url);
    }

}
