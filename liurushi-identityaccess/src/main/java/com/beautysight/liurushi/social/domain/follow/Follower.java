/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.social.domain.follow;

import com.beautysight.liurushi.common.domain.ValueObject;
import com.beautysight.liurushi.identityaccess.domain.user.UserView;

/**
 * @author chenlong
 * @since 1.0
 */
public class Follower extends ValueObject {

    public String followId;
    public UserView.LiteAndStats follower;

}
