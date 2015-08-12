/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.app.command;

import com.beautysight.liurushi.common.app.DPO;
import com.beautysight.liurushi.common.utils.Beans;
import com.beautysight.liurushi.common.utils.PreconditionUtils;
import com.beautysight.liurushi.fundamental.domain.storage.FileMetadata;
import com.beautysight.liurushi.identityaccess.domain.model.User;

/**
 * @author chenlong
 * @since 1.0
 */
public class AvatarDPO extends DPO {

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