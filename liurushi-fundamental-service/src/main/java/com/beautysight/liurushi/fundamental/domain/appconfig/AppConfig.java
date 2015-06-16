/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.fundamental.domain.appconfig;

import com.beautysight.liurushi.common.domain.AbstractEntity;
import org.mongodb.morphia.annotations.Entity;

/**
 * 应用配置
 *
 * @author chenlong
 * @since 1.0
 */
@Entity("app_config")
public class AppConfig extends AbstractEntity {

    private ConfigItemName name;
    private String value;

    public String value() {
        return this.value;
    }

    public enum ConfigItemName {
        sms_android_credential, sms_ios_credential;
    }

}
