/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.domain.model;

import com.beautysight.liurushi.common.domain.AbstractEntity;
import com.beautysight.liurushi.common.domain.ValueObject;
import com.beautysight.liurushi.common.utils.Beans;
import com.beautysight.liurushi.common.utils.Passwords;
import com.beautysight.liurushi.common.utils.PreconditionUtils;
import com.beautysight.liurushi.fundamental.domain.storage.ResourceInStorage;
import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.mongodb.morphia.annotations.Entity;

import java.util.Date;
import java.util.Set;

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
    private Avatar blurredAvatar;

    // 默认组别为业余组
    private Group group = Group.amateur;

    public User() {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    }

    public User(String nickname, Gender gender, String mobile, String email, String plainPwd, Avatar originalAvatar) {
        this.nickname = nickname;
        this.gender = gender;
        this.mobile = mobile;
        this.email = email;
        this.password = new Password(plainPwd);
        this.originalAvatar = originalAvatar;

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

    public Avatar originalAvatar() {
        return this.originalAvatar;
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

    public void setMaxAvatar(ResourceInStorage avatar, Integer spec) {
        this.maxAvatar = new Avatar(avatar, spec);
    }

    public void setBlurredAvatar(ResourceInStorage avatar) {
        this.blurredAvatar = new Avatar(avatar);
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

    public static class Avatar extends ValueObject {

        private static final Set<Integer> avatarSpecs = Sets.newHashSet(300, 270, 210, 108, 96, 66, 110, 180);

        private String key;
        private String hash;
        private Integer spec;

        public Avatar() {
        }

        public Avatar(ResourceInStorage resource) {
            this(resource, null);
        }

        public Avatar(ResourceInStorage resource, Integer spec) {
            this.key = resource.getKey();
            this.hash = resource.getHash();
            this.spec = spec;
        }

        public String key() {
            return this.key;
        }

        public void validate() {
            commonValidate();
            validateSpec(this.spec);
        }

        public void validateAsOriginal() {
            commonValidate();
        }

        public static void validateSpec(int spec) {
            Preconditions.checkArgument(avatarSpecs.contains(spec),
                    String.format("Expected avatar.spec: %s, but actual %s",
                            avatarSpecs, spec));
        }

        private void commonValidate() {
            PreconditionUtils.checkRequired("Avatar.key", key);
            PreconditionUtils.checkRequired("Avatar.hash", hash);
        }

    }

}