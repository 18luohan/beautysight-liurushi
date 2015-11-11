/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.domain.user;

import com.beautysight.liurushi.common.domain.AbstractEntity;
import com.beautysight.liurushi.common.utils.PreconditionUtils;
import org.mongodb.morphia.annotations.Entity;

/**
 * @author chenlong
 * @since 1.0
 */
@Entity(value = "client_apps", noClassnameStored = true)
public class ClientApp extends AbstractEntity {

    private AppName name;
    private Integer internalVersion;
    private String releaseVersion;
    private Tag tag;
    private String downloadUrl;
    private UpgradePolicy upgradePolicy = UpgradePolicy.recommend;
    private String upgradePrompt;

    public void validateAsPO() {
        PreconditionUtils.checkRequired("app.name", name);
        PreconditionUtils.checkRequired("app.internalVersion", internalVersion);
    }

    public void setUpgradePolicy(UpgradePolicy upgradePolicy) {
        this.upgradePolicy = upgradePolicy;
    }

    public AppName name() {
        return this.name;
    }

    public Integer internalVersion() {
        return this.internalVersion;
    }

    public enum AppName {
        android_playplus, ios_playplus
    }

    public enum UpgradePolicy {
        force, recommend, not_recommend
    }

    public enum Tag {
        latest
    }

}
