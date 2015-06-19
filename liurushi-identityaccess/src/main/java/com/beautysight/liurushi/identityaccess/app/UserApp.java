/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.app;

import com.beautysight.liurushi.common.ex.EntityNotFoundException;
import com.beautysight.liurushi.fundamental.domain.storage.ResourceInStorage;
import com.beautysight.liurushi.fundamental.domain.storage.StorageService;
import com.beautysight.liurushi.identityaccess.app.command.LoginCommand;
import com.beautysight.liurushi.identityaccess.app.command.SignUpCommand;
import com.beautysight.liurushi.identityaccess.app.presentation.AccessTokenPresentation;
import com.beautysight.liurushi.identityaccess.app.presentation.DownloadUrlPresentation;
import com.beautysight.liurushi.identityaccess.app.presentation.UserExistPresentation;
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

    public DownloadUrlPresentation issueDownloadAvatarUrl(int expectedSpec, UserClient thisUserClient) {
        Optional<User.Avatar> theAvatar = thisUserClient.user().specificAvatar(expectedSpec);

        String downloadUrl;
        if (theAvatar.isPresent()) {
            downloadUrl = storageService.issueDownloadUrl(theAvatar.get().key());
        } else {
            ResourceInStorage resource = storageService.zoomImageTo(expectedSpec,
                    thisUserClient.user().originalAvatar().key());
            User.Avatar newAvatar = new User.Avatar(resource.key, resource.hash, expectedSpec);
            userRepo.addAvatarFor(thisUserClient.user().id(), newAvatar);
            downloadUrl = resource.url;
        }

        // 更新 request context或缓存
        return DownloadUrlPresentation.from(downloadUrl);
    }

}
