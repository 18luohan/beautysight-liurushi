/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.domain.model;

import com.beautysight.liurushi.common.domain.ValueObject;
import com.google.common.base.Optional;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;

/**
 * @author chenlong
 * @since 1.0
 */
public class UserProfile extends ValueObject {

    private ObjectId id;
    private String nickname;
    private Gender gender;
    private String mobile;
    private String email;
    private User.Group group;
    private User.Avatar originalAvatar;
    private User.Avatar maxAvatar;

    public ObjectId id() {
        return this.id;
    }

    public String nickname() {
        return this.nickname;
    }

    public Gender gender() {
        return this.gender;
    }

    public String mobile() {
        return this.mobile;
    }

    public String email() {
        return this.email;
    }

    public User.Group group() {
        return this.group;
    }

    public Optional<String> originalAvatarKey() {
        if (this.originalAvatar == null
                || StringUtils.isBlank(this.originalAvatar.key())) {
            return Optional.absent();
        }
        return Optional.of(this.originalAvatar.key());
    }

    public Optional<String> maxAvatarKey() {
        if (this.maxAvatar == null
                || StringUtils.isBlank(this.maxAvatar.key())) {
            return Optional.absent();
        }
        return Optional.of(this.maxAvatar.key());
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