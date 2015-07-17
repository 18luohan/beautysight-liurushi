/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.domain.model.content;

import com.beautysight.liurushi.common.domain.ValueObject;

/**
 * @author chenlong
 * @since 1.0
 */
public class Author extends ValueObject {

    public String id;
    public String nickname;
    public String originalAvatarUrl;
    public String maxAvatarUrl;

    public Author(String id, String nickname, String originalAvatarUrl, String maxAvatarUrl) {
        this.id = id;
        this.nickname = nickname;
        this.originalAvatarUrl = originalAvatarUrl;
        this.maxAvatarUrl = maxAvatarUrl;
    }

}