/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.app;

import com.beautysight.liurushi.common.ex.EntityNotFoundException;
import com.beautysight.liurushi.common.ex.IllegalDomainModelStateException;
import com.beautysight.liurushi.common.utils.Regexp;
import com.beautysight.liurushi.fundamental.app.DownloadUrlPresentation;
import com.beautysight.liurushi.fundamental.domain.storage.StorageService;
import com.beautysight.liurushi.identityaccess.app.command.LoginCommand;
import com.beautysight.liurushi.identityaccess.app.command.SignUpCommand;
import com.beautysight.liurushi.identityaccess.app.presentation.AccessTokenPresentation;
import com.beautysight.liurushi.identityaccess.app.presentation.UserExistPresentation;
import com.beautysight.liurushi.identityaccess.app.presentation.UserProfilePresentation;
import com.beautysight.liurushi.identityaccess.domain.model.AccessToken;
import com.beautysight.liurushi.identityaccess.domain.model.Device;
import com.beautysight.liurushi.identityaccess.domain.model.User;
import com.beautysight.liurushi.identityaccess.domain.model.UserClient;
import com.beautysight.liurushi.identityaccess.domain.repo.AccessTokenRepo;
import com.beautysight.liurushi.identityaccess.domain.repo.UserRepo;
import com.beautysight.liurushi.identityaccess.domain.service.AccessTokenService;
import com.beautysight.liurushi.identityaccess.domain.service.UserService;
import com.google.common.base.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chenlong
 * @since 1.0
 */
@Service
public class UserApp {

    @Autowired
    private UserService userService;
    @Autowired
    private AccessTokenService accessTokenService;
    @Autowired
    private StorageService storageService;

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private AccessTokenRepo accessTokenRepo;

    public UserExistPresentation checkIfUserExistWith(String mobile) {
        return new UserExistPresentation(userRepo.withMobile(mobile).isPresent());
    }

    public AccessTokenPresentation signUp(SignUpCommand command) {
        User user = userService.signUp(command.user.toUser());
        Device device = userService.saveOrAddUserToDevice(command.device.toDevice(), user);
        AccessToken bearerToken = accessTokenRepo.save(AccessToken.issueBearerTokenFor(user, device));
        return AccessTokenPresentation.from(bearerToken);
    }

    public AccessTokenPresentation login(LoginCommand command) {
        User theUser = userService.login(command.user.toUser(), command.user.password);
        Device theDevice = userService.saveOrAddUserToDevice(command.device.toDevice(), theUser);
        AccessToken accessToken = accessTokenService.issueOrRefreshBearerTokenFor(theUser, theDevice);
        return AccessTokenPresentation.from(accessToken);
    }

    public AccessTokenPresentation logout(UserClient userClient) {
        Optional<AccessToken> theToken = accessTokenRepo.bearerTokenIssuedFor(userClient.userId(), userClient.deviceId());
        if (theToken.isPresent()) {
            return AccessTokenPresentation.from(accessTokenService.exchangeForBasicToken(theToken.get()));
        }
        throw new EntityNotFoundException("Expect bearer token present, but actual absent");
    }

    public UserProfilePresentation getUserProfile(String userId) {
        User user = userRepo.findOne(userId);
        String originalAvatarUrl = storageService.issueDownloadUrl(user.originalAvatarKey());
        String maxAvatarUrl = storageService.issueDownloadUrl(user.maxAvatar().key());
        return UserProfilePresentation.from(user.toUserProfile(), originalAvatarUrl, maxAvatarUrl);
    }

    public DownloadUrlPresentation issueDownloadUrlOfMaxAvatar(UserClient thisUserClient) {
        User.Avatar theAvatar = thisUserClient.user().maxAvatar();

        String downloadUrl;
        if (theAvatar != null) {
            downloadUrl = storageService.issueDownloadUrl(theAvatar.key());
            // TODO 更新 request context或缓存
            return DownloadUrlPresentation.from(downloadUrl);
        }

        throw new IllegalDomainModelStateException("User has no max avatar, mobile: %s", thisUserClient.user().mobile());
    }

    public void setUsersGroupToProfessional(List<String> mobiles) {
        List<String> mobilesWithCallingCode = new ArrayList<>(mobiles.size());
        for (String mobile : mobiles) {
            if (Regexp.isChinaMobileWithoutCallingCode(mobile)) {
                mobilesWithCallingCode.add("+86-" + mobile);
            }
        }
        userRepo.setUsersGroupToProfessional(mobilesWithCallingCode);
    }

}
