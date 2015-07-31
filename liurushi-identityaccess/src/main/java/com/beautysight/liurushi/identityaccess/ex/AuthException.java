/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.ex;

import com.beautysight.liurushi.common.ex.ApplicationException;
import com.beautysight.liurushi.common.ex.Error;
import com.beautysight.liurushi.identityaccess.common.AuthErrorId;

/**
 * Here is Javadoc.
 * <p/>
 * Created by chenlong on 2015-05-14.
 *
 * @author chenlong
 * @since 1.0
 */
public class AuthException extends ApplicationException {

    public AuthException(Error.Id errorId, String msgFormat, Object... msgArgs) {
        super(errorId, msgFormat, msgArgs);
    }

    public AuthException(Throwable cause, String msgFormat, Object... msgArgs) {
        super(AuthErrorId.illegal_access_token, cause, msgFormat, msgArgs);
    }

}
