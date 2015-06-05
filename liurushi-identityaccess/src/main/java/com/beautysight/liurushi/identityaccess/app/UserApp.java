/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.app;

import com.beautysight.liurushi.common.ex.EntityNotFoundException;
import com.beautysight.liurushi.identityaccess.app.command.LoginCommand;
import com.beautysight.liurushi.identityaccess.app.command.SignUpCommand;
import com.beautysight.liurushi.identityaccess.app.presentation.AccessTokenPresentation;
import com.beautysight.liurushi.identityaccess.domain.model.AccessToken;
import com.beautysight.liurushi.identityaccess.domain.model.Device;
import com.beautysight.liurushi.identityaccess.domain.model.User;
import com.beautysight.liurushi.identityaccess.domain.model.UserClient;
import com.beautysight.liurushi.identityaccess.domain.repo.AccessTokenRepo;
import com.beautysight.liurushi.identityaccess.domain.service.AccessTokenService;
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
public class UserApp {

    @Autowired
    private UserService userService;
    @Autowired
    private AccessTokenService accessTokenService;
    @Autowired
    private AccessTokenRepo accessTokenRepo;

    public AccessTokenPresentation signUp(SignUpCommand command) {
        command.validate();
        User user = userService.signUp(command.user.toUser());
        Device device = userService.saveOrAddUserToDevice(command.device.toDevice(), user);
        AccessToken bearerToken = accessTokenRepo.save(AccessToken.issueBearerTokenFor(user, device));
        return AccessTokenPresentation.from(bearerToken);
    }

    public AccessTokenPresentation login(LoginCommand command) {
        command.validate();
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





}
