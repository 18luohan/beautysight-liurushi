/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.fundamental.app;

import com.beautysight.liurushi.fundamental.domain.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
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
        String url = storageService.issueDownloadUrlWithFileOps(
                command.key, command.expiry, command.instructions, command.savedAsKey);
        return DownloadUrlPresentation.from(url);
    }

}
