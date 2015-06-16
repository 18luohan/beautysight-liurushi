/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.domain.model;

import com.beautysight.liurushi.common.domain.ValueObject;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.bson.types.ObjectId;

/**
 * Here is Javadoc.
 * <p/>
 * Created by chenlong on 2015-05-30.
 *
 * @author chenlong
 * @since 1.0
 */
public class UserClient extends ValueObject {

    private User.UserLite user;
    private User.Type userType;
    private ObjectId deviceId;

    private UserClient() {
    }

    public UserClient(User user, Device device, AccessToken.Type tokenType) {
        if (user != null) {
            this.user = user.toUserLite();
        }
        this.deviceId = device.id();
        this.userType = determineUserType(tokenType);
    }

    public UserClient(User user, Device device, User.Type userType) {
        if (user != null) {
            this.user = user.toUserLite();
        }
        this.deviceId = device.id();
        this.userType = userType;
    }

    public static UserClient newVisitor(Device device) {
        return new UserClient(null, device, User.Type.visitor);
    }

    public ObjectId userId() {
        return this.user.id();
    }

    public User.UserLite user() {
        return this.user;
    }

    public User.Type userType() {
        return userType;
    }

    public ObjectId deviceId() {
        return this.deviceId;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SIMPLE_STYLE);
    }

    private User.Type determineUserType(AccessToken.Type tokenType) {
        if (tokenType == AccessToken.Type.Basic) {
            return User.Type.visitor;
        } else if (tokenType == AccessToken.Type.Bearer) {
            return User.Type.member;
        } else {
            throw new IllegalArgumentException("No corresponding user type to access token type:" + tokenType);
        }
    }

}
