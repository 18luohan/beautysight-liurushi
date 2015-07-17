/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.fundamental.domain.storage;

import com.beautysight.liurushi.common.domain.ValueObject;
import com.beautysight.liurushi.common.utils.PreconditionUtils;

/**
 * @author chenlong
 * @since 1.0
 */
public class ResourceInStorage extends ValueObject {

    private String key;
    private String hash;
    private String url;

    public void setUrl(String url) {
        this.url = url;
    }

    public String getKey() {
        return key;
    }

    public String getUrl() {
        return url;
    }

    public String getHash() {
        return hash;
    }

    public void validate() {
        PreconditionUtils.checkRequired("ResourceInStorage.key", key);
        PreconditionUtils.checkRequired("ResourceInStorage.hash", hash);
    }

}
