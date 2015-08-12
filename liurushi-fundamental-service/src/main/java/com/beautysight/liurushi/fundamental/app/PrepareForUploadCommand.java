/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.fundamental.app;

import com.beautysight.liurushi.common.app.Command;
import com.beautysight.liurushi.common.utils.PreconditionUtils;
import com.beautysight.liurushi.fundamental.domain.storage.FileMetadata;

/**
 * @author chenlong
 * @since 1.0
 */
public class PrepareForUploadCommand implements Command {

    /**
     * 要上传的文件个数
     */
    public Integer filesCount;
    public FileMetadata.Type type = FileMetadata.Type.image;

    public void validate() {
        PreconditionUtils.checkGreaterThanZero("filesCount", filesCount);
    }

}