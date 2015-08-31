/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.fundamental.domain.appconfig;

import com.beautysight.liurushi.common.ex.EntityNotFoundException;
import com.beautysight.liurushi.test.utils.Reflections;
import com.google.common.base.Optional;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Here is Javadoc.
 * <p/>
 * Created by chenlong on 2015-06-16.
 *
 * @author chenlong
 * @since 1.0
 */
@RunWith(JMockit.class)
public class AppConfigServiceTest {

    @Tested
    private AppConfigService appConfigService;

    @Injectable
    AppConfigRepo appConfigRepo;

    @Test
    public void getConfigItemValue() {
        new Expectations() {{
            appConfigRepo.withName(anyString);
//            AppConfig appConfig = Jsons.toObject(
//                    Files.fileInSameDirWith(this.getClass(), "sms_android_credential.json"),
//                    AppConfig.class);
            AppConfig appConfig = new AppConfig();
            Reflections.setField(appConfig, "name", AppConfig.ItemName.sms_android_credential);
            Reflections.setField(appConfig, "value", "{ \"appKey\":\"here is appKey\", \"appSecret\":\"here is appSecret\" }");
            result = Optional.of(appConfig);
        }};

        final AppCredentialInThirdParty credential = appConfigService.getItemValue(
                AppConfig.ItemName.sms_android_credential);

        new Verifications() {{
            assertNotNull(credential);
            assertEquals("SMS android appKey not equal",
                    "here is appKey",
                    Reflections.getField(credential, "appKey"));
        }};
    }

    @Test(expected = EntityNotFoundException.class)
    public void getConfigItemValueButAbsent() {
        new Expectations() {{
            appConfigRepo.withName(anyString);
            result = Optional.absent();
        }};

        appConfigService.getItemValue(
                AppConfig.ItemName.sms_android_credential);
    }

    @Test
    public void getSharingH5ShotsNum() {
        new Expectations() {{
            appConfigRepo.withName(anyString);
            AppConfig appConfig = new AppConfig();
            Reflections.setField(appConfig, "name", AppConfig.ItemName.sharing_h5_shots_num);
            Reflections.setField(appConfig, "value", "{ \"val\":\"8\" }");
            result = Optional.of(appConfig);
        }};

        IntegerVal val = appConfigService.getItemValue(AppConfig.ItemName.sharing_h5_shots_num);
        assertNotNull(val);
        assertEquals(8, val.val().intValue());
    }

}