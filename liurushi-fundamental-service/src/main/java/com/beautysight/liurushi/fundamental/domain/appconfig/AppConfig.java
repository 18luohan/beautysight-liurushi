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

    private ItemName name;
    private String value;

    public String value() {
        return this.value;
    }

    public enum ItemName {
        sms_android_credential, sms_ios_credential, sharing_h5_shots_num,
        image_fit_device_strategy;
    }

}