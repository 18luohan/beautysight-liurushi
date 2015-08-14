/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.domain.model;

import com.beautysight.liurushi.common.domain.ValueObject;
import org.bson.types.ObjectId;

/**
 * @author chenlong
 * @since 1.0
 */
public class UserLite extends ValueObject {

    private ObjectId id;
    private String nickname;
    private String mobile;
    private User.Group group;

    public ObjectId id() {
        return this.id;
    }

    public String nickname() {
        return this.nickname;
    }

    public String mobile() {
        return this.mobile;
    }

    public User.Group group() {
        return this.group;
    }

    @Override
    public String toString() {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("{id:").append(id)
                .append(",mobile:").append(mobile)
                .append(",...}");
        return strBuilder.toString();
    }

}