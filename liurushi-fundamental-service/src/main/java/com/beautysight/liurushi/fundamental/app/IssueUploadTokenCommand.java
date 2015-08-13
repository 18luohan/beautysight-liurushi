/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.fundamental.app;

import com.beautysight.liurushi.common.app.Command;
import com.beautysight.liurushi.fundamental.domain.storage.UploadOptions;

import java.util.Map;

/**
 * Here is Javadoc.
 * <p/>
 * Created by chenlong on 2015-05-18.
 *
 * @author chenlong
 * @since 1.0
 */
public class IssueUploadTokenCommand implements Command {

    public String key;
    public String keyExpr;
    public Map<String, Object> returnBody;
    public String checksum;

    public UploadOptions toUploadOptions() {
        return UploadOptions.newInstance();
    }

}
