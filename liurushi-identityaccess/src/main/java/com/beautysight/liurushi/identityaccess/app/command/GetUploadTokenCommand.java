/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.app.command;

import com.beautysight.liurushi.common.app.Command;
import com.beautysight.liurushi.common.utils.Jsons;
import com.beautysight.liurushi.fundamental.domain.storage.UploadOptions;
import org.springframework.util.CollectionUtils;

import java.util.Map;

/**
 * Here is Javadoc.
 * <p/>
 * Created by chenlong on 2015-05-18.
 *
 * @author chenlong
 * @since 1.0
 */
public class GetUploadTokenCommand implements Command {

    public String key;
    public String keyExpr;
    public Map<String, Object> returnBody;
    public String checksum;

    public UploadOptions toUploadOptions() {
        UploadOptions options = UploadOptions.newInstance().key(key)
                .saveKey(keyExpr).checksum(checksum);
        if (!CollectionUtils.isEmpty(returnBody)) {
            options.returnBody(Jsons.toJsonString(returnBody));
        }
        return options;
    }

}
