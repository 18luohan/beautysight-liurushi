/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.domain.model;

import com.beautysight.liurushi.common.domain.AbstractEntity;
import com.beautysight.liurushi.common.domain.ValueObject;
import com.beautysight.liurushi.common.utils.Beans;
import com.beautysight.liurushi.common.utils.Passwords;
import com.beautysight.liurushi.fundamental.domain.storage.FileMetadata;
import com.beautysight.liurushi.identityaccess.app.presentation.UserDPO;
import com.google.common.base.Optional;
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

    @Reference(idOnly = true)
    private FileMetadata originalAvatar;
    @Reference(idOnly = true)
    private FileMetadata headerPhoto;

    private Avatar maxAvatar;

    // 默认组别为业余组
    private Group group = Group.amateur;

    public User() {
    }

    public User(Optional<String> nickname, Gender gender, String mobile, String email, String plainPwd, Optional<FileMetadata> avatar) {
        this.gender = gender;
        this.mobile = mobile;
        this.email = email;
        this.password = new Password(plainPwd);

        if (nickname.isPresent()) {
            this.nickname = nickname.get();
        } else {
            this.nickname = mobile;
        }

        if (avatar.isPresent()) {
            this.originalAvatar = avatar.get();
        }
    }

    public boolean isGivenPwdCorrect(String plainPwd) {
        return this.password.isGivenPwdCorrect(plainPwd);
    }

    public boolean hasAvatar() {
        return (this.originalAvatar != null);
    }

    public Optional<FileMetadata> originalAvatar() {
        return Optional.fromNullable(originalAvatar);
    }

    public void changeOriginalAvatar(FileMetadata newAvatar) {
        this.originalAvatar = newAvatar;
    }

    public Optional<FileMetadata> headerPhoto() {
        return Optional.fromNullable(headerPhoto);
    }

    public void changeHeaderPhoto(FileMetadata newHeaderPhoto) {
        this.headerPhoto = newHeaderPhoto;
    }

    public Optional<Avatar> maxAvatar() {
        return Optional.fromNullable(maxAvatar);
    }

    public void setMaxAvatar(Avatar maxAvatar) {
        this.maxAvatar = maxAvatar;
    }

    public String nickname() {
        return this.nickname;
    }

    public Group group() {
        return this.group;
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

    public void edit(UserDPO userDPO) {
        if (StringUtils.isNotBlank(userDPO.nickname)) {
            this.nickname = userDPO.nickname;
        }
        if (StringUtils.isNotBlank(userDPO.email)) {
            this.email = userDPO.email;
        }
        if (userDPO.gender != null) {
            this.gender = userDPO.gender;
        }
        this.modifiedAt = new Date();
    }

    public UserLite toUserLite() {
        UserLite userLite = new UserLite();
        Beans.copyProperties(this, userLite);
        return userLite;
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