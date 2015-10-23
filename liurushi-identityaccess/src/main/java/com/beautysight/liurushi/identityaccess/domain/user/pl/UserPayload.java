/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.domain.user.pl;

import com.beautysight.liurushi.common.app.Payload;
import com.beautysight.liurushi.common.utils.Beans;
import com.beautysight.liurushi.fundamental.app.FileMetadataPayload;
import com.beautysight.liurushi.identityaccess.domain.user.Gender;
import com.beautysight.liurushi.identityaccess.domain.user.User;

/**
 * @author chenlong
 * @since 1.0
 */
public class UserPayload extends Payload {

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
    public FileMetadataPayload avatar;

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