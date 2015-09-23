/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.domain.work;

import com.beautysight.liurushi.common.domain.ValueObject;

/**
 * @author chenlong
 * @since 1.0
 */
public class Author extends ValueObject {

    public String id;
    public String nickname;
    public String maxAvatarUrl;
    public Group group;

    public Author(String id, String nickname, String groupAsStr, String maxAvatarUrl) {
        this.id = id;
        this.nickname = nickname;
        this.group = Group.valueOf(groupAsStr);
        this.maxAvatarUrl = maxAvatarUrl;
    }

    public enum Group {
        professional, amateur
    }

}