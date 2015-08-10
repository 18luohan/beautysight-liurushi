/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.rest.community;

import com.beautysight.liurushi.community.domain.model.work.Author;
import com.beautysight.liurushi.identityaccess.domain.model.UserProfile;
import com.beautysight.liurushi.rest.common.RequestContext;

/**
 * @author chenlong
 * @since 1.0
 */
public class Authors {

    public static Author currentAuthor() {
        UserProfile currentUser = RequestContext.getUserProfile();
        return new Author(currentUser.id().toString(), currentUser.nickname(),
                currentUser.group().toString());
    }

}
