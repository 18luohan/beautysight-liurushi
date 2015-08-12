/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.app.command;

import com.beautysight.liurushi.common.app.DPO;
import com.beautysight.liurushi.identityaccess.domain.model.Gender;
import com.beautysight.liurushi.identityaccess.domain.model.User;
import com.google.common.base.Optional;

/**
 * @author chenlong
 * @since 1.0
 */
public class UserDPO extends DPO {

    public String nickname;
    public Gender gender;
    public String mobile;
    public String email;
    public String password;
    public String confirmPassword;

    public AvatarDPO avatar;

    public User toUser() {
        return new User(nickname, gender, mobile, email, password);
    }

    public Optional<User.Avatar> getAvatar() {
        if (avatar == null) {
            return Optional.absent();
        }
        return Optional.of(avatar.toAvatar());
    }

}