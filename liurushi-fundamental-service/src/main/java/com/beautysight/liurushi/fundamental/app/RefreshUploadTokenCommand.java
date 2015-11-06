/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.fundamental.app;

import com.beautysight.liurushi.common.app.Command;
import com.beautysight.liurushi.common.utils.PreconditionUtils;

import java.util.List;

/**
 * @author chenlong
 * @since 1.0
 */
public class RefreshUploadTokenCommand implements Command {

    public List<String> fileIds;

    public void validate() {
        PreconditionUtils.checkRequired("fileIds", fileIds);
        for (int i = 0; i < fileIds.size(); i++) {
            PreconditionUtils.checkRequired(String.format("fileIds[%s]", i), fileIds.get(i));
        }
    }

}