/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.domain.user;

import com.beautysight.liurushi.common.domain.ValueObject;
import com.beautysight.liurushi.common.utils.PreconditionUtils;
import org.mongodb.morphia.annotations.Reference;

/**
 * @author chenlong
 * @since 1.0
 */
public class AppInstallation extends ValueObject {

    @Reference(value = "appId", lazy = true, idOnly = true)
    private ClientApp app;
    private OS os;

    private AppInstallation() {
    }

    public AppInstallation(ClientApp app, OS os) {
        this.app = app;
        this.os = os;
    }

    public static class OS extends ValueObject {
        private Type type;
        private String version;
        // Android系统的各种分发版
        private String distribution;

        public void validate() {
            PreconditionUtils.checkRequired("os.type", type);
            PreconditionUtils.checkRequired("os.version", version);
            PreconditionUtils.checkRequired("os.distribution", distribution);
        }

        public enum Type {
            android, ios
        }
    }

}
