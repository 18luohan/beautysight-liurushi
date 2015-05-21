/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.app.command;

import com.beautysight.liurushi.common.app.Command;
import com.beautysight.liurushi.common.utils.PreconditionUtils;

/**
 * Here is Javadoc.
 * <p/>
 * Created by chenlong on 2015-05-18.
 *
 * @author chenlong
 * @since 1.0
 */
public class GetDownloadUrlCommand implements Command {

    public String key;
    public String instructions;
    public String savedAsKey;
    public int expiry;

    public void validate() {
        PreconditionUtils.checkRequired("key", key);
    }

}
