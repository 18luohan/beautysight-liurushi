/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.fundamental.app;

import com.beautysight.liurushi.fundamental.domain.storage.FileMetadata;
import com.beautysight.liurushi.fundamental.domain.storage.FileMetadataService;
import com.beautysight.liurushi.fundamental.domain.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        List<FileMetadata> files = fileMetadataService.createLogicFiles(command.filesCount, command.type, command.bizCategory);
        return new PrepareForUploadPresentation(files);
    }

    public RefreshUploadTokenVM refreshUploadTokens(RefreshUploadTokenCommand command) {
        Map<String, String> fileIdToUploadTokenMap = new HashMap<>(command.fileIds.size());
        for (String fileId : command.fileIds) {
            FileMetadata file = fileMetadataService.fileWithId(fileId);
            String uploadToken = storageService.issueUploadToken(file.key());
            fileIdToUploadTokenMap.put(fileId, uploadToken);
        }
        return new RefreshUploadTokenVM(fileIdToUploadTokenMap);
    }

    public DownloadUrl downloadUrl(IssueDownloadUrlCommand command) {
        command.validate();
        String url = storageService.downloadUrl(command.key);
        return DownloadUrl.from(url);
    }

}
