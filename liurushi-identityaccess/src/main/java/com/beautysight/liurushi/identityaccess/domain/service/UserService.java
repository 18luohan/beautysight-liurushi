/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.domain.service;

import com.beautysight.liurushi.common.ex.DuplicateEntityException;
import com.beautysight.liurushi.common.ex.EntityNotFoundException;
import com.beautysight.liurushi.common.utils.Logs;
import com.beautysight.liurushi.fundamental.domain.storage.ResourceInStorage;
import com.beautysight.liurushi.fundamental.domain.storage.StorageService;
import com.beautysight.liurushi.identityaccess.common.UserErrorId;
import com.beautysight.liurushi.identityaccess.domain.model.Device;
import com.beautysight.liurushi.identityaccess.domain.model.User;
import com.beautysight.liurushi.identityaccess.domain.repo.DeviceRepo;
import com.beautysight.liurushi.identityaccess.domain.repo.UserRepo;
import com.google.common.base.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private DeviceRepo deviceRepo;
    @Autowired
    private StorageService storageService;

    private static final ExecutorService executor = Executors.newCachedThreadPool();

    public User signUp(final User newUser) {
        Optional<User> theUser = userRepo.withMobile(newUser.mobile());
        if (theUser.isPresent()) {
            throw new DuplicateEntityException(UserErrorId.user_already_exist,
                    "user already exist with mobile: " + newUser.mobile());
        }

        newUser.setLastLoginToNow();
        final User savedUser = userRepo.save(newUser);

        executor.submit(new Runnable() {
            @Override
            public void run() {
                ResourceInStorage avatar300 = storageService.zoomImageTo(300, savedUser.originalAvatarKey());
                Logs.debug(logger, "Asynchronously produced 300 px avatar for user: %s", savedUser.mobile());
                int radius = 30, segma = 20;
                ResourceInStorage blurredAvatar = storageService.blurImageAccordingTo(radius, segma, savedUser.originalAvatarKey());
                Logs.debug(logger, "Asynchronously produced blurred avatar background for user: %s", savedUser.mobile());
                savedUser.setMaxAvatar(avatar300, 300);
                savedUser.setBlurredAvatar(blurredAvatar);
                userRepo.merge(savedUser);
            }
        });

        return savedUser;
    }

    public User login(User loggingInUser, String plainPwd) {
        Optional<User> theUser = userRepo.withMobile(loggingInUser.mobile());
        if (!theUser.isPresent() || !theUser.get().isGivenPwdCorrect(plainPwd)) {
            throw new EntityNotFoundException(UserErrorId.user_not_exist_or_pwd_incorrect,
                    "user not exist or pwd incorrect");
        }

        loggingInUser.setLastLoginToNow();
        userRepo.updateLastLoginTime(loggingInUser);
        return theUser.get();
    }

    public Device saveOrAddUserToDevice(Device device, User user) {
        Optional<Device> theDevice = deviceRepo.withImei(device.imei());
        if (theDevice.isPresent()) {
            deviceRepo.addDeviceUser(device.imei(), user);
            return theDevice.get();
        }
        device.addUser(user);
        return deviceRepo.save(device);
    }

//    public void avatarDownloadUrl(User.UserLite userLite, int size) {
//        User.Avatar avatar = userLite.avatar();
//    }
//
//    private void produceAvatarThumbnail(String originalAvatarKey, int thumbnailSize) {
//        storageService.issueDownloadUrl()
//    }

}
