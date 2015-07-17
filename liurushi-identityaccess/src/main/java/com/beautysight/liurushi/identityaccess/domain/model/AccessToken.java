/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.domain.model;

import com.beautysight.liurushi.common.domain.AbstractEntity;
import com.beautysight.liurushi.common.ex.BusinessException;
import com.google.common.base.Preconditions;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Reference;

import java.util.Date;
import java.util.UUID;

/**
 * @author chenlong
 * @since 1.0
 */
@Entity(value = "access_tokens", noClassnameStored = true)
public class AccessToken extends AbstractEntity {

    private static final int ONE_HOUR_SECONDS = 3600;
//    private static final int ONE_HOUR_SECONDS = 30;
    private static final int NEVER_EXPIRES = -1;

    private String accessToken;
    private Type type;
    private String refreshToken;
    private Date refreshedAt;

    private int expiresIn;

    @Reference(value = "userId", lazy = true, idOnly = true)
    private User user;
    @Reference(value = "deviceId", lazy = true, idOnly = true)
    private Device device;

    public AccessToken() {
    }

    public AccessToken(Type tokenType, User user, Device device) {
        this.accessToken = generateToken();
        this.type = tokenType;
        this.user = user;
        this.device = device;
        this.expiresIn = this.determineExpiry();

        if (type == Type.Bearer) {
            this.refreshToken = generateToken();
        }
    }

    public static AccessToken issueBasicTokenFor(Device device) {
        return new AccessToken(Type.Basic, null, device);
    }

    public static AccessToken issueBearerTokenFor(User user, Device device) {
        return new AccessToken(Type.Bearer, user, device);
    }

    public boolean isBasic() {
        return (this.type == Type.Basic);
    }

    public boolean isBearer() {
        return (this.type == Type.Bearer);
    }

    public void refresh() {
        if (this.type != Type.Bearer) {
            throw new BusinessException("Can't refresh %s token", this.type);
        }

        this.accessToken = generateToken();
        this.refreshedAt = new Date();
    }

    public boolean isExpired() {
        if (this.type == Type.Basic) {
            // Basic token never expires
            return false;
        }

        // TODO 为了测试方便，token永久有效
        return false;

//        if (this.type == Type.Bearer) {
//            Date effectiveAt = (refreshedAt == null ? createdAt : refreshedAt);
//            return DateTimes.beforeOrEqualNow(effectiveAt, expiresIn);
//        }
//
//        throw new IllegalEntityStateException("Illegal token type, type: %s, token: %s", type, accessToken);
    }

    public String accessToken() {
        return this.accessToken;
    }

    public String refreshToken() {
        return this.refreshToken;
    }

    public Type type() {
        return this.type;
    }

    public User user() {
        return this.user;
    }

    public Device device() {
        return this.device;
    }

    private String generateToken() {
        return UUID.randomUUID().toString();
    }

    private int determineExpiry() {
        Preconditions.checkState(this.type != null, "token type is null");
        if (Type.Basic == this.type) {
            return NEVER_EXPIRES;
        } else if (Type.Bearer == this.type) {
            return ONE_HOUR_SECONDS;
        } else {
            throw new IllegalStateException("Not supported token type:" + this.type);
        }
    }

    public enum Type {
        Basic, Bearer
    }

}
