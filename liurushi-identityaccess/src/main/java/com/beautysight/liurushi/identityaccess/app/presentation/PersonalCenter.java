/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.app.presentation;

import com.beautysight.liurushi.common.app.PresentationModel;

import java.util.Random;

/**
 * @author chenlong
 * @since 1.0
 */
public class PersonalCenter implements PresentationModel {

    private Integer followersNum;
    private Integer followingsNum;
    private Integer worksNum;
    private Integer favoritesNum;
    private UserDPO userProfile;

    public static PersonalCenter from(UserDPO userProfile) {
        PersonalCenter result = new PersonalCenter();

        Random random = new Random();
        result.followersNum = random.nextInt(10000);
        result.followingsNum = random.nextInt(10000);
        result.worksNum = 0;
        result.favoritesNum = 0;
        result.userProfile = userProfile;

        return result;
    }

}
