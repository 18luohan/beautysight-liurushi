/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.domain.service;

import com.beautysight.liurushi.common.ex.NoUniqueEntityException;
import com.beautysight.liurushi.identityaccess.domain.model.Device;
import com.beautysight.liurushi.identityaccess.domain.model.User;
import com.beautysight.liurushi.identityaccess.domain.repo.DeviceRepository;
import com.beautysight.liurushi.identityaccess.domain.repo.UserRepository;
import com.google.common.base.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * Here is Javadoc.
 * <p/>
 * Created by chenlong on 2015-05-13.
 *
 * @author chenlong
 * @since 1.0
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DeviceRepository deviceRepository;

    public Optional<User> userWithMobilePhone(String mobilePhone) {
        List<User> users = userRepository.withMobilePhone(mobilePhone);
        if (CollectionUtils.isEmpty(users)) {
            return Optional.absent();
        }

        if (users.size() > 1) {
            throw new NoUniqueEntityException("Found multiple users with mobilePhone: " + mobilePhone);
        }

        return Optional.of(users.get(0));
    }

    public Optional<Device> deviceWithImei(String imei) {
        List<Device> devices = deviceRepository.withImei(imei);
        if (CollectionUtils.isEmpty(devices)) {
            return Optional.absent();
        }

        if (devices.size() > 1) {
            throw new NoUniqueEntityException("Found multiple devices with imei: " + imei);
        }

        return Optional.of(devices.get(0));
    }

    public void saveOrUpdateDevice(Device device, User user) {
        device.addUser(user);
    }

}
