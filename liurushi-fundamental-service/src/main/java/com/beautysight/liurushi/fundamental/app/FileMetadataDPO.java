/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.fundamental.app;

import com.beautysight.liurushi.common.app.DPO;
import com.beautysight.liurushi.common.utils.PreconditionUtils;
import com.beautysight.liurushi.fundamental.domain.storage.FileMetadata;

/**
 * @author chenlong
 * @since 1.0
 */
public class FileMetadataDPO extends DPO {

    public String id;
    public String key;
    public String hash;
    public String url;

    public FileMetadataDPO() {
    }

    public FileMetadataDPO(String id, String key) {
        this.id = id;
        this.key = key;
    }

    public void validate() {
        PreconditionUtils.checkRequired("id", id);
        PreconditionUtils.checkRequired("hash", hash);
    }

    public FileMetadata toDomainModel() {
        FileMetadata domainModel = new FileMetadata();
        domainModel.setId(this.id);
        domainModel.setHash(this.hash);
        return domainModel;
    }

}