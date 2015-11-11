/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.domain.user;

import com.google.common.base.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author chenlong
 * @since 1.0
 */
@Service
public class DeviceService {

    @Autowired
    private DeviceRepo deviceRepo;

    public void saveOrUpdate(Device device, AppInstallation.OS os, ClientApp installedApp) {
        Optional<Device> theDevice = deviceRepo.withImei(device.imei());
        if (!theDevice.isPresent()) {
            device.setAppInstall(new AppInstallation(installedApp, os));
            deviceRepo.save(device);
            return;
        }

        if (!theDevice.get().appInstall().isPresent()) {
            theDevice.get().setAppInstall(new AppInstallation(installedApp, os));
            deviceRepo.merge(theDevice.get());
        }
    }

}
