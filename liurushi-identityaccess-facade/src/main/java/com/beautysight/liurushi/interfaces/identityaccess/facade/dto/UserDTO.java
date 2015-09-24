/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.interfaces.identityaccess.facade.dto;

/**
 * @author chenlong
 * @since 1.0
 */
public class UserDTO implements DTO {

    public String id;
    public String nickname;
    public Group group;
    public String commonAvatarUrl;
    public String maxAvatarUrl;

    public enum Group {
        professional, amateur
    }

}