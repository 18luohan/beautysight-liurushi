/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.domain.model;

import com.beautysight.liurushi.common.domain.AbstractEntity;
import com.beautysight.liurushi.common.domain.ValueObject;
import com.beautysight.liurushi.common.utils.Passwords;
import com.beautysight.liurushi.common.utils.PreconditionUtils;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    private Avatar avatar;
    private List<Avatar> avatars = new ArrayList<>();

    private User() {
    }

    public User(String nickname, Gender gender, String mobilePhone, String plainPwd, Avatar avatar) {
        this.nickname = nickname;
        this.gender = gender;
        this.mobilePhone = mobilePhone;
        this.password = new Password(plainPwd);
        this.avatars.add(avatar);
    }

    public boolean isGivenPwdCorrect(String plainPwd) {
        return this.password.isGivenPwdCorrect(plainPwd);
    }

    public String mobilePhone() {
        return this.mobilePhone;
    }

    public void setLastLoginToNow() {
        this.lastLogin = new Date();
    }

    public Date lastLogin() {
        return this.lastLogin;
    }

    public void addAvatar(Avatar avatar) {

    }

    public Avatar avatar() {
        // TODO 应该返回何种大小的图像
        return this.avatars.get(0);
    }

    public UserLite toUserLite() {
        UserLite userLite = new UserLite();
        userLite.id = this.id();
        userLite.nickname = this.nickname;
        userLite.avatars = this.avatars;
        return userLite;
    }

    public static class UserLite extends ValueObject {
        private ObjectId id;
        private String nickname;
        private Avatar avatar;
        private List<Avatar> avatars;

        public ObjectId id() {
            return this.id;
        }

        public Avatar avatar() {
            return this.avatar;
        }

        public List<Avatar> avatars() {
            return this.avatars;
        }
    }

    public static class Avatar extends ValueObject {
        private String key;
        private String hash;
        private int size;

        public String key() {
            return this.key;
        }

        public void validate() {
            PreconditionUtils.checkRequired("Avatar.key", key);
            PreconditionUtils.checkRequired("Avatar.hash", hash);
            PreconditionUtils.checkGreaterThanZero("Avatar.size", size);
        }
    }

    public enum Type {
        visitor, member
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
