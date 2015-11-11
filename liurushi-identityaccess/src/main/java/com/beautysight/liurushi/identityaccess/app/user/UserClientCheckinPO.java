/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.app.user;

import com.beautysight.liurushi.common.utils.PreconditionUtils;
import com.beautysight.liurushi.identityaccess.domain.user.AppInstallation;
import com.beautysight.liurushi.identityaccess.domain.user.ClientApp;
import com.beautysight.liurushi.identityaccess.domain.user.Device;

/**
 * @author chenlong
 * @since 1.0
 */
public class UserClientCheckInPO {

    public ClientApp installedApp;
    public AppInstallation.OS os;
    public Device device;

    public void validate() {
        PreconditionUtils.checkRequired("installedApp", installedApp);
        PreconditionUtils.checkRequired("os", os);
        PreconditionUtils.checkRequired("device", device);
        installedApp.validateAsPO();
        os.validate();
        device.validateAsPO();
    }

}
