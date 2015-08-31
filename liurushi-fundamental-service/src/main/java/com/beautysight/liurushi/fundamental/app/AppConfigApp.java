/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.fundamental.app;

import com.beautysight.liurushi.fundamental.domain.appconfig.AppConfig;
import com.beautysight.liurushi.fundamental.domain.appconfig.AppConfigService;
import com.beautysight.liurushi.fundamental.domain.appconfig.AppCredentialInThirdParty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author chenlong
 * @since 1.0
 */
@Service
public class AppConfigApp {

    @Autowired
    private AppConfigService appConfigService;

    public AppCredentialInThirdParty smsAndroidCredential() {
        return appConfigService.getItemValue(AppConfig.ItemName.sms_android_credential);
    }

    public AppCredentialInThirdParty smsIOSCredential() {
        return appConfigService.getItemValue(AppConfig.ItemName.sms_ios_credential);
    }

}
