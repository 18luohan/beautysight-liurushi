/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.fundamental.app;

import com.beautysight.liurushi.fundamental.domain.storage.FileMetadata;
import com.beautysight.liurushi.fundamental.domain.storage.FileMetadataService;
import com.beautysight.liurushi.fundamental.domain.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author chenlong
 * @since 1.0
 */
@Service
public class StorageApp {

    @Autowired
    private StorageService storageService;
    @Autowired
    private FileMetadataService fileMetadataService;

    public PrepareForUploadPresentation prepareForUpload(PrepareForUploadCommand command) {
        String uploadToken = storageService.issueUploadToken();
        List<FileMetadata> files = fileMetadataService.createLogicFiles(command.filesCount, command.type);
        return PrepareForUploadPresentation.from(uploadToken, files);
    }

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
