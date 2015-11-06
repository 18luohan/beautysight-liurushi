/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.fundamental.infrastructure.persistence.mongo;

import com.beautysight.liurushi.fundamental.domain.storage.FileMetadata;
import com.beautysight.liurushi.fundamental.domain.storage.FileMetadataRepo;
import com.google.common.base.Optional;
import org.springframework.stereotype.Repository;

/**
 * @author chenlong
 * @since 1.0
 */
@Repository
public class FileMetadataRepoImpl extends AbstractMongoRepository<FileMetadata> implements FileMetadataRepo {

    @Override
    public Optional<FileMetadata> get(String id) {
        return Optional.fromNullable(findOne(toMongoId(id)));
    }

    @Override
    protected Class<FileMetadata> entityClass() {
        return FileMetadata.class;
    }

}
