/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.domain.model;

import com.beautysight.liurushi.common.domain.AbstractEntity;
import com.beautysight.liurushi.common.ex.BusinessException;
import com.beautysight.liurushi.common.ex.IllegalDomainStateException;
import com.beautysight.liurushi.common.utils.DateTimes;
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
    private static final int NEVER_EXPIRES = -1;

    private Type type;
    private String accessToken;
    private int expiresIn;
    private String refreshToken;
    private Date refreshedAt;
    // 上一个access token
    private String lastAccessToken;

    private Boolean isValid = true;
    private Date modifiedAt;

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

    public void refresh() {
        if (this.type != Type.Bearer) {
            throw new BusinessException("Can't refresh %s token", this.type);
        }

        // 先将当前access token另存为last access token
        this.lastAccessToken = this.accessToken;
        this.accessToken = generateToken();
        this.isValid = Boolean.TRUE;
        this.refreshedAt = new Date();
        this.modifiedAt = this.refreshedAt;
    }

    public void invalidate() {
        this.isValid = Boolean.FALSE;
        this.modifiedAt = new Date();
    }

    public boolean isInvalid() {
        // 是否过期是延迟计算的
        return (!isValid || isExpired());
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

    private boolean isExpired() {
        if (this.type == Type.Basic) {
            // Basic token never expires
            return false;
        }

        if (this.type == Type.Bearer) {
            Date effectiveAt = (refreshedAt == null ? createdAt : refreshedAt);
            return DateTimes.beforeOrEqualNow(effectiveAt, expiresIn);
        }

        throw new IllegalDomainStateException("Illegal token type, type: %s, token: %s", type, accessToken);
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
