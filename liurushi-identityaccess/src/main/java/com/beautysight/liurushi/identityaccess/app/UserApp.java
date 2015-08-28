/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.app;

import com.beautysight.liurushi.common.ex.EntityNotFoundException;
import com.beautysight.liurushi.common.utils.Regexp;
import com.beautysight.liurushi.fundamental.app.DownloadUrlPresentation;
import com.beautysight.liurushi.fundamental.app.FileMetadataDPO;
import com.beautysight.liurushi.fundamental.domain.storage.FileMetadata;
import com.beautysight.liurushi.fundamental.domain.storage.StorageService;
import com.beautysight.liurushi.identityaccess.app.cache.UserProfileCache;
import com.beautysight.liurushi.identityaccess.app.command.LoginCommand;
import com.beautysight.liurushi.identityaccess.app.command.LogoutCommand;
import com.beautysight.liurushi.identityaccess.app.command.ResetPasswordCommand;
import com.beautysight.liurushi.identityaccess.app.command.SignUpCommand;
import com.beautysight.liurushi.identityaccess.app.presentation.*;
import com.beautysight.liurushi.identityaccess.common.UserErrorId;
import com.beautysight.liurushi.identityaccess.domain.model.AccessToken;
import com.beautysight.liurushi.identityaccess.domain.model.Device;
import com.beautysight.liurushi.identityaccess.domain.model.User;
import com.beautysight.liurushi.identityaccess.domain.model.UserLite;
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

    public UserExistPresentation checkIfUserExistWith(String mobileOrUnionId, User.Origin origin) {
        origin = (origin == null ? User.Origin.self : origin);
        String globalId;
        if (origin.isSelf()) {
            String mobile = mobileOrUnionId;
            globalId = User.calculateGlobalId(null, origin, mobile);
        } else {
            String unionId = mobileOrUnionId;
            globalId = User.calculateGlobalId(unionId, origin, null);
        }
        return new UserExistPresentation(userRepo.withGlobalId(globalId).isPresent());
    }

    public SignUpOrLoginPresentation signUp(SignUpCommand command) {
        User user = userService.signUp(command.user.toUser());
        Device device = userService.saveOrAddUserToDevice(command.device.toDevice(), user);
        AccessToken accessToken = accessTokenRepo.save(AccessToken.issueBearerTokenFor(user, device));

        UserDPO userDPO = translateToDPOFrom(user);
        AccessTokenPresentation accessTokenPresentation = AccessTokenPresentation.from(accessToken);
        cacheUserProfileForNewSession(accessToken, user.toUserLite());
        return new SignUpOrLoginPresentation(userDPO, accessTokenPresentation);
    }

    public SignUpOrLoginPresentation login(LoginCommand command) {
        command.user.defaultToSelfIfOriginAbsent();
        User user = userService.login(command.user.origin, command.user.mobile, command.user.password, command.user.unionId);
        Device device = userService.saveOrAddUserToDevice(command.device.toDevice(), user);
        AccessToken accessToken = accessTokenService.issueOrRefreshBearerTokenFor(user, device);

        UserDPO userDPO = translateToDPOFrom(user);
        AccessTokenPresentation accessTokenPresentation = AccessTokenPresentation.from(accessToken);
        cacheUserProfileForNewSession(accessToken, user.toUserLite());
        return new SignUpOrLoginPresentation(userDPO, accessTokenPresentation);
    }

    public void logout(LogoutCommand command) {
        accessTokenService.invalidate(command.accessToken, command.type);
        // 从缓存中删除已失效的token
        oAuthApp.evictAccessToken(command.accessToken, command.type);
        // 删除缓存的用户个人资料
        userProfileCache.evictBy(command.accessToken, command.type);
    }

    public UserLite getCurrentUserProfile(final AccessToken.Type type, final String accessToken) {
        return userProfileCache.getIfAbsentLoad(accessToken, type, new Callable<UserLite>() {
            @Override
            public UserLite call() throws Exception {
                User user = accessTokenService.getUserBy(accessToken, type);
                return user.toUserLite();
            }
        });
    }

    public UserDPO getUserProfile(String userId) {
        User user = userRepo.findOne(userId);
        return translateToDPOFrom(user);
    }

    public UserDPO editUserProfile(UserDPO userDPO) {
        User user = userRepo.findOne(userDPO.id);
        user.edit(userDPO);
        userRepo.merge(user);
        return translateToDPOFrom(user);
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

    public DownloadUrlPresentation changeUserOriginalAvatar(String userId, FileMetadataDPO newAvatar) {
        User user = userService.changeUserOriginalAvatar(userId, newAvatar.toDomainModel());
        return DownloadUrlPresentation.from(storageService.issueDownloadUrl(user.maxAvatar().get().key()));
    }

    public DownloadUrlPresentation changeUserHeaderPhoto(String userId, FileMetadataDPO newHeaderPhoto) {
        FileMetadata headerPhoto = userService.changeUserHeaderPhoto(userId, newHeaderPhoto.toDomainModel());
        return DownloadUrlPresentation.from(storageService.issueDownloadUrl(headerPhoto.key()));
    }

    public PersonalCenter getUserPersonalCenter(String userId) {
        User user = userRepo.findOne(userId);
        return PersonalCenter.from(translateToDPOFrom(user));
    }

    public void resetPassword(ResetPasswordCommand command) {
        Optional<User> user = userRepo.withMobile(command.mobile);
        if (!user.isPresent()) {
            throw new EntityNotFoundException(UserErrorId.user_not_exist,
                    "user with mobile(%s) not exist", command.mobile);
        }

        user.get().resetPassword(command.password);
        userRepo.merge(user.get());
    }

    private UserDPO translateToDPOFrom(User user) {
        String originalAvatarUrl = null;
        String headerPhotoUrl = null;
        String maxAvatarUrl = null;

        if (user.originalAvatar().isPresent()) {
            originalAvatarUrl = storageService.issueDownloadUrl(user.originalAvatar().get().key());
        }
        if (user.headerPhoto().isPresent()) {
            headerPhotoUrl = storageService.issueDownloadUrl(user.headerPhoto().get().key());
        }
        if (user.maxAvatar().isPresent()) {
            maxAvatarUrl = storageService.issueDownloadUrl(user.maxAvatar().get().key());
        }

        return UserDPO.from(user, originalAvatarUrl, maxAvatarUrl, headerPhotoUrl);
    }

    /**
     * 为新开始的会话缓存用户个人资料
     *
     * @param accessToken
     * @param userLite
     */
    private void cacheUserProfileForNewSession(AccessToken accessToken, UserLite userLite) {
        userProfileCache.put(accessToken.accessToken(), accessToken.type(), userLite);
    }

}