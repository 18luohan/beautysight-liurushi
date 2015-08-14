/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.fundamental.domain.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public FileMetadata createOneLogicFile(FileMetadata.Type type) {
        FileMetadata aNewFile = FileMetadata.newFile(type);
        fileMetadataRepo.save(aNewFile);
        return aNewFile;
    }

    public List<FileMetadata> createLogicFiles(int filesCount, FileMetadata.Type type) {
        List<FileMetadata> files = new ArrayList<>();
        for (int i = 0; i < filesCount; i++) {
            files.add(FileMetadata.newFile(type));
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

}
