/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.app;

import com.beautysight.liurushi.common.ex.IllegalDomainStateException;
import com.beautysight.liurushi.common.utils.Regexp;
import com.beautysight.liurushi.fundamental.app.DownloadUrlPresentation;
import com.beautysight.liurushi.fundamental.domain.storage.StorageService;
import com.beautysight.liurushi.identityaccess.app.cache.UserProfileCache;
import com.beautysight.liurushi.identityaccess.app.command.LoginCommand;
import com.beautysight.liurushi.identityaccess.app.command.LogoutCommand;
import com.beautysight.liurushi.identityaccess.app.command.SignUpCommand;
import com.beautysight.liurushi.identityaccess.app.presentation.AccessTokenPresentation;
import com.beautysight.liurushi.identityaccess.app.presentation.SignUpOrLoginPresentation;
import com.beautysight.liurushi.identityaccess.app.presentation.UserExistPresentation;
import com.beautysight.liurushi.identityaccess.app.presentation.UserProfilePresentation;
import com.beautysight.liurushi.identityaccess.domain.model.AccessToken;
import com.beautysight.liurushi.identityaccess.domain.model.Device;
import com.beautysight.liurushi.identityaccess.domain.model.User;
import com.beautysight.liurushi.identityaccess.domain.model.UserProfile;
import com.beautysight.liurushi.identityaccess.domain.repo.AccessTokenRepo;
import com.beautysight.liurushi.identityaccess.domain.repo.UserRepo;
import com.beautysight.liurushi.identityaccess.domain.service.AccessTokenService;
import com.beautysight.liurushi.identityaccess.domain.service.UserService;
import com.google.common.base.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * @author chenlong
 * @since 1.0
 */
@Service
public class UserApp {

    private static final Logger logger = LoggerFactory.getLogger(UserApp.class);

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
    @Autowired
    private OAuthApp oAuthApp;

    private UserProfileCache userProfileCache = new UserProfileCache();

    public UserExistPresentation checkIfUserExistWith(String mobile) {
        return new UserExistPresentation(userRepo.withMobile(mobile).isPresent());
    }

    public SignUpOrLoginPresentation signUp(SignUpCommand command) {
        User user = userService.signUp(command.user.toUser());
        Device device = userService.saveOrAddUserToDevice(command.device.toDevice(), user);
        AccessToken accessToken = accessTokenRepo.save(AccessToken.issueBearerTokenFor(user, device));
        AccessTokenPresentation accessTokenPresentation = AccessTokenPresentation.from(accessToken);
        // 再次查询以获取最新的User对象
        user = userRepo.findOne(user.id());
        UserProfilePresentation userProfilePresentation = translateToPresentationFrom(user.toUserProfile());
        cacheUserProfileForNewSession(accessToken, user.toUserProfile());
        return new SignUpOrLoginPresentation(userProfilePresentation, accessTokenPresentation);
    }

    public SignUpOrLoginPresentation login(LoginCommand command) {
        User theUser = userService.login(command.user.mobile, command.user.password);
        Device theDevice = userService.saveOrAddUserToDevice(command.device.toDevice(), theUser);
        AccessToken accessToken = accessTokenService.issueOrRefreshBearerTokenFor(theUser, theDevice);
        AccessTokenPresentation accessTokenPresentation = AccessTokenPresentation.from(accessToken);
        UserProfilePresentation userProfilePresentation = translateToPresentationFrom(theUser.toUserProfile());
        cacheUserProfileForNewSession(accessToken, theUser.toUserProfile());
        return new SignUpOrLoginPresentation(userProfilePresentation, accessTokenPresentation);
    }

    public void logout(LogoutCommand command) {
        accessTokenService.invalidate(command.accessToken, command.type);
        // 从缓存中删除已失效的token
        oAuthApp.evictAccessToken(command.accessToken, command.type);
        // 删除缓存的用户个人资料
        userProfileCache.evictBy(command.accessToken, command.type);
    }

    public UserProfilePresentation getCurrentUserProfilePresentation(AccessToken.Type type, String accessToken) {
        UserProfile userProfile = getCurrentUserProfile(type, accessToken);
        return translateToPresentationFrom(userProfile);
    }

    public UserProfilePresentation getGivenUserProfile(String userId) {
        User user = userRepo.findOne(userId);
        return translateToPresentationFrom(user.toUserProfile());
    }

    public UserProfile getCurrentUserProfile(final AccessToken.Type type, final String accessToken) {
        return userProfileCache.getIfAbsentLoad(accessToken, type, new Callable<UserProfile>() {
            @Override
            public UserProfile call() throws Exception {
                User user = accessTokenService.getUserBy(type.toString(), accessToken);
                return user.toUserProfile();
            }
        });
    }

    public DownloadUrlPresentation issueDownloadUrlOfMaxAvatar(AccessToken.Type type, String accessToken) {
        UserProfile userProfile = getCurrentUserProfile(type, accessToken);
        Optional<String> maxAvatarKey = userProfile.maxAvatarKey();
        if (maxAvatarKey.isPresent()) {
            return DownloadUrlPresentation.from(storageService.issueDownloadUrl(maxAvatarKey.get()));
        }

        throw new IllegalDomainStateException("User has no max avatar, mobile: %s", userProfile.mobile());
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

    private UserProfilePresentation translateToPresentationFrom(UserProfile userProfile) {
        String originalAvatarUrl = null;
        Optional<String> originalAvatarKey = userProfile.originalAvatarKey();
        if (originalAvatarKey.isPresent()) {
            originalAvatarUrl = storageService.issueDownloadUrl(originalAvatarKey.get());
        }

        String maxAvatarUrl = null;
        Optional<String> maxAvatarKey = userProfile.maxAvatarKey();
        if (maxAvatarKey.isPresent()) {
            maxAvatarUrl = storageService.issueDownloadUrl(maxAvatarKey.get());
        }
        return UserProfilePresentation.from(userProfile, originalAvatarUrl, maxAvatarUrl);
    }

    /**
     * 为新开始的会话缓存用户个人资料
     *
     * @param accessToken
     * @param userProfile
     */
    private void cacheUserProfileForNewSession(AccessToken accessToken, UserProfile userProfile) {
        userProfileCache.put(accessToken.accessToken(), accessToken.type(), userProfile);
    }


}
