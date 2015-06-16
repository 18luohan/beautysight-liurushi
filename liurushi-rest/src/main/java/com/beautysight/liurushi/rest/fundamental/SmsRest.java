/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.rest.fundamental;

import com.beautysight.liurushi.fundamental.app.AppConfigApp;
import com.beautysight.liurushi.fundamental.domain.appconfig.AppCredentialInThirdParty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chenlong
 * @since 1.0
 */
@RestController
@RequestMapping("/sms")
public class SmsRest {

    @Autowired
    private AppConfigApp appConfigApp;

    @RequestMapping(value = "/android_credential", method = RequestMethod.GET)
    public AppCredentialInThirdParty smsAndroidCredential() {
        return appConfigApp.smsAndroidCredential();
    }

    @RequestMapping(value = "/ios_credential", method = RequestMethod.GET)
    public AppCredentialInThirdParty smsIOSCredential() {
        return appConfigApp.smsIOSCredential();
    }

}
