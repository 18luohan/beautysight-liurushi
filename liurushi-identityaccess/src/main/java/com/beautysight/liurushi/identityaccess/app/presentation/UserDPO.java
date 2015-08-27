/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.app.presentation;

import com.beautysight.liurushi.common.app.DPO;
import com.beautysight.liurushi.common.utils.Beans;
import com.beautysight.liurushi.fundamental.app.FileMetadataDPO;
import com.beautysight.liurushi.identityaccess.domain.model.Gender;
import com.beautysight.liurushi.identityaccess.domain.model.User;

/**
 * @author chenlong
 * @since 1.0
 */
public class UserDPO extends DPO {

    public String id;
    public String unionId;
    public String nickname;
    public Gender gender;
    public String mobile;
    public String email;
    public User.Group group;
    public User.Origin origin;

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
        defaultToSelfIfOriginAbsent();

        User user = new User();
        Beans.copyProperties(this, user);
        user.setGlobalId(unionId, origin, mobile);
        user.changeOriginalAvatar((avatar == null ? null : avatar.toDomainModel()));

        // 如果用户是通过我们自己的app注册的，就需要设置密码
        if (this.origin.isSelf()) {
            user.resetPassword(password);
        }

        return user;
    }

    public void defaultToSelfIfOriginAbsent() {
        if (this.origin == null) {
            this.origin = User.Origin.self;
        }
    }

}