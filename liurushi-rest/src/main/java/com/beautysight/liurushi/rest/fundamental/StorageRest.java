/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.rest.fundamental;

import com.beautysight.liurushi.fundamental.app.*;
import com.beautysight.liurushi.rest.common.APIs;
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
@RequestMapping(APIs.STORAGE_V1)
public class StorageRest {

    @Autowired
    private StorageApp storageApp;

    @RequestMapping(value = "/actions/prepare_for_upload", method = RequestMethod.POST)
    @VisitorApiPermission
    public PrepareForUploadPresentation prepareForUpload(@RequestBody PrepareForUploadCommand command) {
        command.validate();
        return storageApp.prepareForUpload(command);
    }

    @RequestMapping(value = "/upload_token", method = RequestMethod.POST)
    @VisitorApiPermission
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