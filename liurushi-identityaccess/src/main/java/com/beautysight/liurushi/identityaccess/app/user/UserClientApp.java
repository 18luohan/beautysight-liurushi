/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.app.user;

import com.beautysight.liurushi.common.ex.BusinessException;
import com.beautysight.liurushi.identityaccess.domain.user.ClientApp;
import com.beautysight.liurushi.identityaccess.domain.user.ClientAppRepo;
import com.beautysight.liurushi.identityaccess.domain.user.DeviceService;
import com.google.common.base.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author chenlong
 * @since 1.0
 */
@Service
public class UserClientApp {

    private static final Logger logger = LoggerFactory.getLogger(UserClientApp.class);

    @Autowired
    private ClientAppRepo clientAppRepo;
    @Autowired
    private DeviceService deviceService;

    public UserClientCheckInRO checkIn(UserClientCheckInPO checkInPO) {
        Optional<ClientApp> installedApp = clientAppRepo.with(
                checkInPO.installedApp.name(), checkInPO.installedApp.internalVersion());
        if (!installedApp.isPresent()) {
            throw new BusinessException("App with name %s and internalVersion %s not exist",
                    checkInPO.installedApp.name(), checkInPO.installedApp.internalVersion());
        }

        deviceService.saveOrUpdate(checkInPO.device, checkInPO.os, installedApp.get());

        Optional<ClientApp> latestApp = clientAppRepo.with(
                checkInPO.installedApp.name(), ClientApp.Tag.latest);
        if (latestApp.isPresent()) {
            if (latestApp.get().internalVersion()
                    == installedApp.get().internalVersion()) {
                installedApp.get().setUpgradePolicy(ClientApp.UpgradePolicy.not_recommend);
            }
        } else {
            logger.error("Latest app with name %s not exist", checkInPO.installedApp.name());
        }

        return new UserClientCheckInRO(latestApp.orNull(), installedApp.get());
    }

}
