/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.fundamental.infrastructure.persistence.mongo;

import com.beautysight.liurushi.fundamental.domain.storage.FileMetadata;
import com.beautysight.liurushi.fundamental.domain.storage.FileMetadataRepo;
import org.springframework.stereotype.Repository;

/**
 * @author chenlong
 * @since 1.0
 */
@Repository
public class FileMetadataRepoImpl extends AbstractMongoRepository<FileMetadata> implements FileMetadataRepo {

    @Override
    protected Class<FileMetadata> entityClass() {
        return FileMetadata.class;
    }

}
