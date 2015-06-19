/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.rest.identityaccess;

import com.beautysight.liurushi.identityaccess.app.StorageApp;
import com.beautysight.liurushi.identityaccess.app.command.IssueDownloadUrlCommand;
import com.beautysight.liurushi.identityaccess.app.command.IssueUploadTokenCommand;
import com.beautysight.liurushi.identityaccess.app.presentation.DownloadUrlPresentation;
import com.beautysight.liurushi.identityaccess.app.presentation.UploadTokenPresentation;
import com.beautysight.liurushi.rest.permission.VisitorApiPermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chenlong
 * @since 1.0
 */
@RestController
@RequestMapping("/storage")
public class StorageRest {

    @Autowired
    private StorageApp storageApp;

    @RequestMapping(value = "/upload_token", method = RequestMethod.POST)
    @VisitorApiPermission(true)
    public UploadTokenPresentation issueUploadToken(
            @RequestBody(required = false) IssueUploadTokenCommand command) {
        if (command == null) {
            command = new IssueUploadTokenCommand();
        }
        return storageApp.issueUploadToken(command);
    }

    @RequestMapping(value = "/download_url", method = RequestMethod.POST)
    public DownloadUrlPresentation issueDownloadUrl(@RequestBody IssueDownloadUrlCommand command) {
        return storageApp.issueDownloadUrl(command);
    }

}
