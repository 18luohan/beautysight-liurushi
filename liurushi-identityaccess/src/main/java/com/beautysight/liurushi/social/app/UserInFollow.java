/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.social.app;

import com.beautysight.liurushi.common.app.PresentationModel;
import com.beautysight.liurushi.identityaccess.domain.dpo.UserDPO;

/**
 * @author chenlong
 * @since 1.0
 */
public class UserInFollow implements PresentationModel {

    private String id;
    private String nickname;
    private String maxAvatarUrl;
    private Integer followersNum;
    private String followId;

    public UserInFollow(String followId, UserDPO userDPO) {
        this.id = userDPO.id;
        this.nickname = userDPO.nickname;
        this.maxAvatarUrl = userDPO.maxAvatarUrl;
        this.followId = followId;
        this.followersNum = userDPO.stats.followersNum;
    }

}