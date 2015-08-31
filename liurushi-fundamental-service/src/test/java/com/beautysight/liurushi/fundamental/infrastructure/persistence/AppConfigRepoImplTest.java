/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.fundamental.infrastructure.persistence;

import com.beautysight.liurushi.fundamental.domain.appconfig.AppConfig;
import com.beautysight.liurushi.fundamental.domain.appconfig.AppConfigRepo;
import com.beautysight.liurushi.test.SpringBasedAppTest;
import com.beautysight.liurushi.test.mongo.Cleanup;
import com.beautysight.liurushi.test.mongo.Prepare;
import com.beautysight.liurushi.test.utils.Reflections;
import com.google.common.base.Optional;
import static org.junit.Assert.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author chenlong
 * @since 1.0
 */
public class AppConfigRepoImplTest extends SpringBasedAppTest {

    @Autowired
    private AppConfigRepo appConfigRepo;

    @Test
    @Prepare
    @Cleanup
    public void smsCredentialConfig() {
        Optional<AppConfig> appConfig = appConfigRepo.withName(
                AppConfig.ItemName.sms_android_credential.toString());
        assertTrue(appConfig.isPresent());
        assertEquals("App config item name not equal",
                AppConfig.ItemName.sms_android_credential,
                Reflections.getField(appConfig.get(), "name"));
    }

    @Test
    @Prepare
    @Cleanup
    public void h5SharingShotsNum() {
        Optional<AppConfig> appConfig = appConfigRepo.withName(
                AppConfig.ItemName.sharing_h5_shots_num.toString());
        assertTrue(appConfig.isPresent());
    }

}