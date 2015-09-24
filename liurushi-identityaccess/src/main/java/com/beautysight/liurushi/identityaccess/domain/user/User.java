/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.domain.user;

import com.beautysight.liurushi.common.domain.AbstractEntity;
import com.beautysight.liurushi.common.domain.ValueObject;
import com.beautysight.liurushi.common.utils.Passwords;
import com.beautysight.liurushi.fundamental.domain.storage.FileMetadata;
import com.beautysight.liurushi.identityaccess.domain.user.pl.UserPayload;
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

    protected String globalId;
    protected String nickname;
    protected Gender gender;
    protected String mobile;
    protected String email;
    protected Password password;
    protected Date lastLogin;

    @Reference(idOnly = true)
    private FileMetadata originalAvatar;
    @Reference(idOnly = true)
    private FileMetadata headerPhoto;
    private Avatar maxAvatar;

    /**
     * 用户组。默认为业余组
     */
    protected Group group = Group.amateur;
    /**
     * 用户来源
     */
    protected Origin origin;
    /**
     * 用户统计信息
     */
    protected Stats stats;

    public User() {
    }

    public static String calculateGlobalId(String unionId, Origin origin, String mobile) {
        if (origin.isSelf()) {
            return mobile;
        }
        return origin + ":" + unionId;
    }

    public void setGlobalId(String globalId, Origin origin, String mobile) {
        this.globalId = calculateGlobalId(globalId, origin, mobile);
    }

    public String globalId() {
        return this.globalId;
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

    public void edit(UserPayload userDPO) {
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

    public void resetPassword(String plainPwd) {
        this.password = new Password(plainPwd);
    }

    public void defaultStatsToZero() {
        this.stats = new Stats();
    }

    public Optional<Stats> stats() {
        return Optional.fromNullable(this.stats);
    }

    public enum Type {
        visitor, member
    }

    public enum Group {
        professional, amateur;

        public String val() {
            return this.toString();
        }
    }

    public enum Origin {
        weixin, sina_weibo, self;

        public boolean isSelf() {
            return (this == self);
        }
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

    public static class Stats extends ValueObject {

        private static final int DEFAULT_VAL = 0;

        private Integer followersNum = DEFAULT_VAL;
        private Integer followingsNum = DEFAULT_VAL;
        private Integer worksNum = DEFAULT_VAL;
        private Integer favoritesNum = DEFAULT_VAL;

        public Integer getFollowersNum() {
            return followersNum;
        }

        public Integer getFollowingsNum() {
            return followingsNum;
        }

        public Integer getWorksNum() {
            return worksNum;
        }

        public Integer getFavoritesNum() {
            return favoritesNum;
        }

    }

}