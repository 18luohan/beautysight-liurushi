/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.fundamental.app;

import com.beautysight.liurushi.common.app.DPO;

/**
 * @author chenlong
 * @since 1.0
 */
public class FileMetadataDPO extends DPO {

    private String id;
    private String key;
    private String hash;
    private String url;

    public FileMetadataDPO(String id, String key) {
        this.id = id;
        this.key = key;
    }

}