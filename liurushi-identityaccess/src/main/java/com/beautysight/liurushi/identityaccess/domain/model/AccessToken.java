/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.domain.model;

import com.beautysight.liurushi.common.domain.AbstractEntity;
import org.mongodb.morphia.annotations.Entity;

import java.util.UUID;

/**
 * Here is Javadoc.
 * <p/>
 * Created by chenlong on 2015-05-14.
 *
 * @author chenlong
 * @since 1.0
 */
@Entity(value = "access_tokens", noClassnameStored = true)
public class AccessToken extends AbstractEntity {

    private String accessToken;
    private String refreshToken;
    private Object userId;
    private Object deviceId;

    private AccessToken() {
    }

    public AccessToken(User user, Device device) {
        this.accessToken = generateToken();
        this.refreshToken = generateToken();
        this.userId = user.id();
        this.deviceId = device.id();
    }

    public String accessToken() {
        return this.accessToken;
    }

    public String refreshToken() {
        return this.refreshToken;
    }

    public static AccessToken issueFor(User user, Device device) {
        return new AccessToken(user, device);
    }

    private String generateToken() {
        return UUID.randomUUID().toString();
    }

}
