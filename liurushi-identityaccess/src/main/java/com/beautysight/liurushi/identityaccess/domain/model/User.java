/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.domain.model;

import com.beautysight.liurushi.common.domain.AbstractEntity;
import com.beautysight.liurushi.common.utils.Passwords;
import org.mongodb.morphia.annotations.Entity;

import java.util.Date;

/**
 * Here is Javadoc.
 * <p/>
 * Created by chenlong on 2015-05-06.
 *
 * @author chenlong
 * @since 1.0
 */
@Entity(value = "users", noClassnameStored = true)
public class User extends AbstractEntity {

    private static final long serialVersionUID = -5710604955737349787L;

    private String nickname;
    private Gender gender;
    private String mobilePhone;
    private Password password;
    private Date lastLogin;

    private User() {
    }

    public User(String nickname, Gender gender, String mobilePhone, String plainPwd) {
        this.nickname = nickname;
        this.gender = gender;
        this.mobilePhone = mobilePhone;
        this.password = new Password(plainPwd);
    }

    public boolean isGivenPwdCorrect(String plainPwd) {
        return this.password.isGivenPwdCorrect(plainPwd);
    }

    private static class Password {
        private String cipherPwd;
        private String salt;

        private Password() {
        }

        private Password(String plainPwd) {
            this.salt = Passwords.nextSalt();
            this.cipherPwd = Passwords.encrypt(plainPwd, salt);
        }

        private boolean isGivenPwdCorrect(String plainPwd) {
            return (cipherPwd.equals(Passwords.encrypt(plainPwd, salt)));
        }
    }

}
