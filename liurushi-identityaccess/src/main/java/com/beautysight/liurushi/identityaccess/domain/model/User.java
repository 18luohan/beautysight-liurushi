/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.domain.model;

import com.beautysight.liurushi.common.domain.AbstractEntity;
import com.beautysight.liurushi.common.domain.ValueObject;
import com.beautysight.liurushi.common.utils.Beans;
import com.beautysight.liurushi.common.utils.Passwords;
import com.beautysight.liurushi.fundamental.domain.storage.FileMetadata;
import org.apache.commons.lang3.StringUtils;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Reference;

import java.util.Date;

/**
 * @author chenlong
 * @since 1.0
 */
@Entity(value = "users", noClassnameStored = true)
public class User extends AbstractEntity {

    private static final long serialVersionUID = -5710604955737349787L;

    private String nickname;
    private Gender gender;
    private String mobile;
    private String email;
    private Password password;
    private Date lastLogin;

    private Avatar originalAvatar;
    private Avatar maxAvatar;

    // 默认组别为业余组
    private Group group = Group.amateur;

    public User() {
    }

    public User(String nickname, Gender gender, String mobile, String email, String plainPwd) {
        this.nickname = nickname;
        this.gender = gender;
        this.mobile = mobile;
        this.email = email;
        this.password = new Password(plainPwd);

        // 如果nickname为空，就将手机号作为昵称
        if (StringUtils.isBlank(this.nickname)) {
            this.nickname = mobile;
        }
    }

    public boolean isGivenPwdCorrect(String plainPwd) {
        return this.password.isGivenPwdCorrect(plainPwd);
    }

    public boolean hasAvatar() {
        return (this.originalAvatar != null);
    }

    public void setOriginalAvatar(Avatar originalAvatar) {
        this.originalAvatar = originalAvatar;
    }

    public String originalAvatarKey() {
        return this.originalAvatar.key();
    }

    public String nickname() {
        return this.nickname;
    }

    public Group group() {
        return this.group;
    }

    public Avatar maxAvatar() {
        return this.maxAvatar;
    }

    public void setMaxAvatar(Avatar maxAvatar) {
        this.maxAvatar = maxAvatar;
    }

    public String mobile() {
        return this.mobile;
    }

    public void setLastLoginToNow() {
        this.lastLogin = new Date();
    }

    public Date lastLogin() {
        return this.lastLogin;
    }

    public UserProfile toUserProfile() {
        UserProfile userProfile = new UserProfile();
        Beans.copyProperties(this, userProfile);
        return userProfile;
    }

    public enum Type {
        visitor, member
    }

    public enum Group {
        professional, amateur
    }

    private static class Password {
        private String cipherPwd;
        private String salt;

        public Password() {
        }

        private Password(String plainPwd) {
            this.salt = Passwords.nextSalt();
            this.cipherPwd = Passwords.encrypt(plainPwd, salt);
        }

        private boolean isGivenPwdCorrect(String plainPwd) {
            return (cipherPwd.equals(Passwords.encrypt(plainPwd, salt)));
        }
    }

    public static class Avatar extends ValueObject {

        @Reference(value = "fileId", idOnly = true)
        private FileMetadata file;
        private Integer spec;

        public Avatar() {
        }

        public Avatar(FileMetadata file) {
            this(file, null);
        }

        public Avatar(FileMetadata file, Integer spec) {
            this.file = file;
            this.spec = spec;
        }

        public FileMetadata file() {
            return this.file;
        }

        public String key() {
            return this.file.key();
        }

        public Integer spec() {
            return this.spec;
        }

    }

}