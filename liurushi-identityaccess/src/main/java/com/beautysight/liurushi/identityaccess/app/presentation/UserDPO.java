/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.app.presentation;

import com.beautysight.liurushi.common.app.DPO;
import com.beautysight.liurushi.common.utils.Beans;
import com.beautysight.liurushi.fundamental.app.FileMetadataDPO;
import com.beautysight.liurushi.fundamental.domain.storage.FileMetadata;
import com.beautysight.liurushi.identityaccess.domain.model.Gender;
import com.beautysight.liurushi.identityaccess.domain.model.User;
import com.google.common.base.Optional;

/**
 * @author chenlong
 * @since 1.0
 */
public class UserDPO extends DPO {

    public String id;
    public String nickname;
    public Gender gender;
    public String mobile;
    public String email;
    public User.Group group;

    // 注册时使用
    public String password;
    public String confirmPassword;
    public FileMetadataDPO avatar;

    public String originalAvatarUrl;
    public String maxAvatarUrl;
    public String headerPhotoUrl;

    public static UserDPO from(User user, String originalAvatarUrl, String maxAvatarUrl, String headerPhotoUrl) {
        UserDPO target = new UserDPO();
        Beans.copyProperties(user, target);
        target.id = user.idAsStr();
        target.group = user.group();
        target.originalAvatarUrl = originalAvatarUrl;
        target.maxAvatarUrl = maxAvatarUrl;
        target.headerPhotoUrl = headerPhotoUrl;
        return target;
    }

    public User toUser() {
        FileMetadata avatarFile = (avatar == null ? null : avatar.toDomainModel());
        return new User(Optional.fromNullable(nickname), gender, mobile, email, password, Optional.fromNullable(avatarFile));
    }

}