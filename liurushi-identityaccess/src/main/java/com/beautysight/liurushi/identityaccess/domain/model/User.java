/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.domain.model;

import com.beautysight.liurushi.common.domain.AbstractEntity;
import com.beautysight.liurushi.common.domain.ValueObject;
import com.beautysight.liurushi.common.utils.Passwords;
import com.beautysight.liurushi.common.utils.PreconditionUtils;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import org.apache.commons.collections.CollectionUtils;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

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
    private String mobile;
    private String email;
    private Password password;
    private Date lastLogin;

    private Avatar originalAvatar;
    private List<Avatar> avatars = new ArrayList<>();

    private User() {
    }

    public User(String nickname, Gender gender, String mobile, String email, String plainPwd, Avatar originalAvatar) {
        this.nickname = nickname;
        this.gender = gender;
        this.mobile = mobile;
        this.email = email;
        this.password = new Password(plainPwd);
        this.originalAvatar = originalAvatar;
    }

    public boolean isGivenPwdCorrect(String plainPwd) {
        return this.password.isGivenPwdCorrect(plainPwd);
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

    public UserLite toUserLite() {
        return new UserLite(this);
    }

    public static class UserLite extends ValueObject {
        private ObjectId id;
        private String nickname;
        private Avatar originalAvatar;
        private List<Avatar> avatars;

        private UserLite(User user) {
            this.id = user.id();
            this.nickname = user.nickname;
            this.originalAvatar = user.originalAvatar;
            this.avatars = user.avatars;
        }

        public ObjectId id() {
            return this.id;
        }

        public Avatar originalAvatar() {
            return this.originalAvatar;
        }

        public Optional<Avatar> specificAvatar(int spec) {
            Avatar.validateSpec(spec);

            if (CollectionUtils.isEmpty(avatars)) {
                Optional.absent();
            }

            for (Avatar avatar : avatars) {
                if (avatar.spec == spec) {
                    return Optional.of(avatar);
                }
            }

            return Optional.absent();
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

    public static class Avatar extends ValueObject {

        private static final Set<Integer> avatarSpecs = Sets.newHashSet(66, 110, 180);

        private String key;
        private String hashVal;
        private int spec;

        public Avatar() {
        }

        public Avatar(String key, String hashVal, int spec) {
            this.key = key;
            this.hashVal = hashVal;
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
            PreconditionUtils.checkGreaterThanZero("Avatar.spec", this.spec);
        }

        public static void validateSpec(int spec) {
            Preconditions.checkArgument(avatarSpecs.contains(spec),
                    String.format("Expected avatar.spec: %s, but actual %s",
                            avatarSpecs, spec));
        }

        private void commonValidate() {
            PreconditionUtils.checkRequired("Avatar.key", key);
            PreconditionUtils.checkRequired("Avatar.hashVal", hashVal);
        }

    }
}
