/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.fundamental.domain.storage;

import com.beautysight.liurushi.common.ex.ApplicationException;
import com.beautysight.liurushi.common.ex.BusinessException;
import com.google.common.base.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author chenlong
 * @since 1.0
 */
@Service
public class FileMetadataService {

    @Autowired
    private FileMetadataRepo fileMetadataRepo;
    @Autowired
    private StorageService storageService;

    public FileMetadata createOneLogicFile(String fileType, FileMetadata.BizCategory bizCategory) {
        return createOneLogicFile(FileMetadata.Type.valueOf(fileType), bizCategory);
    }

    public FileMetadata createOneLogicFile(FileMetadata.Type type, FileMetadata.BizCategory bizCategory) {
        FileMetadata aNewFile = newLogicFile(type, bizCategory);
        fileMetadataRepo.save(aNewFile);
        return aNewFile;
    }

    public List<FileMetadata> createLogicFiles(int filesCount, FileMetadata.Type type, FileMetadata.BizCategory bizCategory) {
        List<FileMetadata> files = new ArrayList<>();
        for (int i = 0; i < filesCount; i++) {
            files.add(newLogicFile(type, bizCategory));
        }
        fileMetadataRepo.save(files);
        return files;
    }

    public FileMetadata updateFileHash(FileMetadata uploadedFile) {
        FileMetadata theFile = fileMetadataRepo.findOne(uploadedFile.id());
        theFile.setHash(uploadedFile.hash());
        fileMetadataRepo.merge(theFile);
        return theFile;
    }

    public void delete(FileMetadata file) {
        FileMetadata theFile = fileMetadataRepo.findOne(file.id());
        theFile.delete();
        fileMetadataRepo.merge(theFile);
    }

    public FileMetadata fileWithId(String id) {
        Optional<FileMetadata> theFile = fileMetadataRepo.get(id);
        if (theFile.isPresent()) {
            return theFile.get();
        }
        throw new BusinessException("file id not exist: %s", id);
    }

    private FileMetadata newLogicFile(FileMetadata.Type type, FileMetadata.BizCategory bizCategory) {
        FileMetadata newFile = FileMetadata.newFile(type, bizCategory);
        newFile.setUploadToken(storageService.issueUploadToken(newFile.key()));
        return newFile;
    }

}
