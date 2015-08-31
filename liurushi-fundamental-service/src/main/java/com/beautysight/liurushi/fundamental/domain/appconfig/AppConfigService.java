/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.fundamental.domain.appconfig;

import com.beautysight.liurushi.common.ex.EntityNotFoundException;
import com.beautysight.liurushi.common.ex.Expected;
import com.beautysight.liurushi.common.utils.Jsons;
import com.google.common.base.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author chenlong
 * @since 1.0
 */
@Service
public class AppConfigService {

    private static final Map<AppConfig.ItemName, Class> configItemToValModelMapping = new HashMap<>();

    @Autowired
    private AppConfigRepo appConfigRepo;

    static {
        configItemToValModelMapping.put(AppConfig.ItemName.sms_android_credential, AppCredentialInThirdParty.class);
        configItemToValModelMapping.put(AppConfig.ItemName.sms_ios_credential, AppCredentialInThirdParty.class);
        configItemToValModelMapping.put(AppConfig.ItemName.sms_ios_credential, AppCredentialInThirdParty.class);
    }

    public <T> T getItemValue(AppConfig.ItemName itemName) {
        Optional<AppConfig> configItem = appConfigRepo.withName(itemName.toString());
        if (configItem.isPresent()) {
            return (T) Jsons.toObject(configItem.get().value(), configItemToValModelMapping.get(itemName));
        }
        throw new EntityNotFoundException(Expected.of(itemName.toString()));
    }

}
