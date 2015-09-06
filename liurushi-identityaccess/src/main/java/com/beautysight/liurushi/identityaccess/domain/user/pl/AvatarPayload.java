/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.domain.user.pl;

import com.beautysight.liurushi.common.app.Payload;
import com.beautysight.liurushi.common.utils.Beans;
import com.beautysight.liurushi.common.utils.PreconditionUtils;
import com.beautysight.liurushi.fundamental.domain.storage.FileMetadata;
import com.beautysight.liurushi.identityaccess.domain.user.User;

/**
 * @author chenlong
 * @since 1.0
 */
public class AvatarPayload extends Payload {

    private String id;
    private String key;
    private String hash;

    public void validate() {
        PreconditionUtils.checkRequired("avatar.id", id);
        PreconditionUtils.checkRequired("avatar.hash", hash);
    }

    public User.Avatar toAvatar() {
        FileMetadata file = FileMetadata.newImageFile();
        Beans.copyProperties(this, file);
        file.setId(id);
        User.Avatar avatar = new User.Avatar(file);
        return avatar;
    }

}