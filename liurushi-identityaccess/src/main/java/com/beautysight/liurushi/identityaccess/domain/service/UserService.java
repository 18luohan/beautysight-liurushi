/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.domain.service;

import com.beautysight.liurushi.common.ex.DuplicateEntityException;
import com.beautysight.liurushi.common.ex.EntityNotFoundException;
import com.beautysight.liurushi.common.utils.AsyncTasks;
import com.beautysight.liurushi.fundamental.domain.storage.FileMetadata;
import com.beautysight.liurushi.fundamental.domain.storage.FileMetadataRepo;
import com.beautysight.liurushi.fundamental.domain.storage.FileMetadataService;
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
    @Autowired
    private FileMetadataService fileMetadataService;
    @Autowired
    private FileMetadataRepo fileMetadataRepo;

    public User signUp(final User newUser) {
        Optional<User> theUser = userRepo.withGlobalId(newUser.globalId());
        if (theUser.isPresent()) {
            throw new DuplicateEntityException(UserErrorId.user_already_exist,
                    "user already exist with mobile: " + newUser.mobile());
        }

        newUser.setLastLoginToNow();
        if (newUser.hasAvatar()) {
            setNewAvatarFor(newUser, newUser.originalAvatar().get());
        }

        return userRepo.save(newUser);
    }

    public User login(String mobile, String plainPwd) {
        Optional<User> theUser = userRepo.withMobile(mobile);
        if (!theUser.isPresent() || !theUser.get().isGivenPwdCorrect(plainPwd)) {
            throw new EntityNotFoundException(UserErrorId.user_not_exist_or_pwd_incorrect,
                    "user not exist or pwd incorrect");
        }

        theUser.get().setLastLoginToNow();
        userRepo.updateLastLoginTime(theUser.get());
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

    public User changeUserOriginalAvatar(String userId, FileMetadata newAvatar) {
        User user = userRepo.findOne(userId);

        if (user.hasAvatar()) {
            // 删除当前头像
            fileMetadataService.delete(user.originalAvatar().get());
            fileMetadataService.delete(user.maxAvatar().get().file());
        }

        // 设置新头像
        setNewAvatarFor(user, newAvatar);

        userRepo.merge(user);
        return user;
    }

    public FileMetadata changeUserHeaderPhoto(String userId, FileMetadata newHeaderPhoto) {
        User user = userRepo.findOne(userId);

        if (user.headerPhoto().isPresent()) {
            // 删除当前 header photo
            fileMetadataService.delete(user.headerPhoto().get());
        }

        FileMetadata theNewFile = fileMetadataService.updateFileHash(newHeaderPhoto);
        user.changeHeaderPhoto(theNewFile);
        userRepo.merge(user);
        return theNewFile;
    }

    private void setNewAvatarFor(User user, FileMetadata newOriginalAvatar) {
        FileMetadata theNewFile = fileMetadataService.updateFileHash(newOriginalAvatar);
        user.changeOriginalAvatar(theNewFile);
        createMaxAvatarFor(user);
    }

    private void createMaxAvatarFor(final User user) {
        final FileMetadata theLogicFile = fileMetadataService.createOneLogicFile(FileMetadata.Type.image);
        final User.Avatar maxAvatar = new User.Avatar(theLogicFile, 300);
        user.setMaxAvatar(maxAvatar);
        AsyncTasks.submit(new Runnable() {
            @Override
            public void run() {
                FileMetadata zoomedFile = storageService.zoomImageTo(
                        maxAvatar.spec(), user.originalAvatar().get().key(), maxAvatar.key());
                logger.debug("Asynchronously produced {} px avatar for user: {}", maxAvatar.spec(), user.mobile());
                theLogicFile.setHash(zoomedFile.hash());
                fileMetadataRepo.merge(theLogicFile);
//                int radius = 30, segma = 20;
//                FileMetadata blurredAvatar = storageService.blurImageAccordingTo(radius, segma, savedUser.originalAvatarKey());
//                logger.debug("Asynchronously produced blurred avatar background for user: {}", savedUser.mobile());
//                savedUser.setBlurredAvatar(blurredAvatar);
            }
        });
    }

}