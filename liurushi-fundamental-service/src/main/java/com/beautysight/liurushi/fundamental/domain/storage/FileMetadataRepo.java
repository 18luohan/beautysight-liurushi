/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.fundamental.domain.storage;

import com.beautysight.liurushi.fundamental.infrastructure.persistence.mongo.MongoRepository;
import com.google.common.base.Optional;

/**
 * @author chenlong
 * @since 1.0
 */
public interface FileMetadataRepo extends MongoRepository<FileMetadata> {

    Optional<FileMetadata> get(String id);

}
