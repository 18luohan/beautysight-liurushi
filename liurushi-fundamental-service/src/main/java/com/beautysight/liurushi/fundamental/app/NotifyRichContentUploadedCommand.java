/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.fundamental.app;

import com.beautysight.liurushi.common.app.Command;
import com.beautysight.liurushi.common.utils.PreconditionUtils;

/**
 * @author chenlong
 * @since 1.0
 */
public class NotifyRichContentUploadedCommand implements Command {

    public String hash;

    public void validate() {
        PreconditionUtils.checkRequired("hash", hash);
    }

}
