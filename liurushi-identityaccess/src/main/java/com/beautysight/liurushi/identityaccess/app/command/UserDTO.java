/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.app.command;

import com.beautysight.liurushi.identityaccess.domain.model.Gender;
import com.beautysight.liurushi.identityaccess.domain.model.User;

/**
 * Here is Javadoc.
 * <p/>
 * Created by chenlong on 2015-05-08.
 *
 * @author chenlong
 * @since 1.0
 */
public class UserDTO {

    public String nickname;
    public Gender gender;
    public String mobilePhone;
    public String password;
    public String confirmPassword;
    public User.Avatar avatar;

    public User toUser() {
        return new User(nickname, gender, mobilePhone, password, avatar);
    }

}
