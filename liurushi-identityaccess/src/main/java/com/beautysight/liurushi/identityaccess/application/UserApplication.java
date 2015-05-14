/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.application;

import com.beautysight.liurushi.common.ex.DuplicateEntityException;
import com.beautysight.liurushi.common.ex.EntityNotFoundException;
import com.beautysight.liurushi.identityaccess.application.command.LoginCommand;
import com.beautysight.liurushi.identityaccess.application.command.SignUpCommand;
import com.beautysight.liurushi.identityaccess.application.presentation.AccessTokenPresentation;
import com.beautysight.liurushi.identityaccess.common.UserErrorId;
import com.beautysight.liurushi.identityaccess.domain.model.AccessToken;
import com.beautysight.liurushi.identityaccess.domain.model.Device;
import com.beautysight.liurushi.identityaccess.domain.model.User;
import com.beautysight.liurushi.identityaccess.domain.repo.AccessTokenRepository;
import com.beautysight.liurushi.identityaccess.domain.repo.DeviceRepository;
import com.beautysight.liurushi.identityaccess.domain.repo.UserRepository;
import com.beautysight.liurushi.identityaccess.domain.service.UserService;
import com.google.common.base.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Here is Javadoc.
 * <p/>
 * Created by chenlong on 2015-05-08.
 *
 * @author chenlong
 * @since 1.0
 */
@Service
public class UserApplication {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DeviceRepository deviceRepository;
    @Autowired
    private AccessTokenRepository accessTokenRepository;

    public AccessTokenPresentation signUp(SignUpCommand command) {
        command.validate();
        Optional<User> userOpt = userService.userWithMobilePhone(command.user.mobilePhone);
        if (userOpt.isPresent()) {
            throw new DuplicateEntityException(UserErrorId.user_already_exist,
                    "user already exist with mobilePhone: " + command.user.mobilePhone);
        }

        User user = userRepository.save(command.user.toUser());
        Device device = null;
        Optional<Device> deviceOpt = userService.deviceWithImei(command.device.imei);
        if (deviceOpt.isPresent()) {
            device = deviceOpt.get();
            deviceRepository.addDeviceUser(command.device.imei, user);
        } else {
            device = command.device.toDevice();
            device.addUser(user);
            deviceRepository.save(device);
        }

        AccessToken accessToken = accessTokenRepository.save(AccessToken.issueFor(user, device));
        return AccessTokenPresentation.from(accessToken);
    }

    public AccessTokenPresentation login(LoginCommand command) {
        command.validate();
        Optional<User> userOpt = userService.userWithMobilePhone(command.user.mobilePhone);
        if (!userOpt.isPresent() || !userOpt.get().isGivenPwdCorrect(command.user.password)) {
            throw new EntityNotFoundException(UserErrorId.user_not_exist_or_pwd_incorrect,
                    "user not exist or pwd incorrect");
        }

        // TODO 更新用户最后登录时间
        User user = userOpt.get();
        Device device;
        Optional<Device> deviceOpt = userService.deviceWithImei(command.device.imei);
        if (deviceOpt.isPresent()) {
            device = deviceOpt.get();
            deviceRepository.addDeviceUser(command.device.imei, user);
        } else {
            device = command.device.toDevice();
            device.addUser(user);
            deviceRepository.save(device);
        }

        // TODO 是否需要重新生成access token? 因为退出时access token会失效
        // TODO 如果用户首次使用该设备，就需要生成access token
        Optional<AccessToken> accessToken = accessTokenRepository.issuedFor(user, device);
        if (accessToken.isPresent()) {
            return AccessTokenPresentation.from(accessToken.get());
        } else {
            AccessToken newAccessToken = accessTokenRepository.save(AccessToken.issueFor(user, device));
            return AccessTokenPresentation.from(newAccessToken);
        }
    }

}
