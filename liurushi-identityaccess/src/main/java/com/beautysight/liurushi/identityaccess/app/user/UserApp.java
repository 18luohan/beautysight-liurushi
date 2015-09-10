/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.app.user;

import com.beautysight.liurushi.common.ex.EntityNotFoundException;
import com.beautysight.liurushi.common.utils.Regexp;
import com.beautysight.liurushi.fundamental.app.DownloadUrl;
import com.beautysight.liurushi.fundamental.app.FileMetadataPayload;
import com.beautysight.liurushi.fundamental.domain.storage.FileMetadata;
import com.beautysight.liurushi.fundamental.domain.storage.StorageService;
import com.beautysight.liurushi.identityaccess.app.auth.OAuthApp;
import com.beautysight.liurushi.identityaccess.common.UserErrorId;
import com.beautysight.liurushi.identityaccess.domain.auth.AccessToken;
import com.beautysight.liurushi.identityaccess.domain.auth.AccessTokenRepo;
import com.beautysight.liurushi.identityaccess.domain.auth.AccessTokenService;
import com.beautysight.liurushi.identityaccess.domain.user.*;
import com.beautysight.liurushi.identityaccess.domain.user.pl.UserPayload;
import com.beautysight.liurushi.social.domain.follow.FollowRepo;
import com.google.common.base.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    @Autowired
    private FollowRepo followRepo;

    public ExistenceOfUserVM checkIfUserExistWith(String mobile) {
        return new ExistenceOfUserVM(userRepo.withMobile(mobile).isPresent());
    }

    public ExistenceOfUserVM checkIfUserExistWith(String mobileOrUnionId, User.Origin origin) {
        origin = (origin == null ? User.Origin.self : origin);
        String globalId;
        if (origin.isSelf()) {
            String mobile = mobileOrUnionId;
            globalId = User.calculateGlobalId(null, origin, mobile);
        } else {
            String unionId = mobileOrUnionId;
            globalId = User.calculateGlobalId(unionId, origin, null);
        }
        return new ExistenceOfUserVM(userRepo.withGlobalId(globalId).isPresent());
    }

    public UserAndAccessTokenVM signUp(SignUpCommand command) {
        User user = userService.signUp(command.user.toUser());
        Device device = userService.saveOrAddUserToDevice(command.device.toDevice(), user);
        AccessToken accessToken = accessTokenRepo.save(AccessToken.issueBearerTokenFor(user, device));
        UserView userView = userService.buildViewOf(user);
        return new UserAndAccessTokenVM(userView.whole(), accessToken);
    }

    public UserAndAccessTokenVM login(LoginCommand command) {
        command.user.defaultToSelfIfOriginAbsent();
        User user = userService.login(command.user.origin, command.user.mobile, command.user.password, command.user.unionId);
        Device device = userService.saveOrAddUserToDevice(command.device.toDevice(), user);
        AccessToken accessToken = accessTokenService.issueOrRefreshAccessTokenFor(user, device);
        UserView userView = userService.buildViewOf(user);
        return new UserAndAccessTokenVM(userView.whole(), accessToken);
    }

    public void logout(String accessToken, AccessToken.Type type) {
        accessTokenService.invalidate(accessToken, type);
        // 从缓存中删除已失效的token
        oAuthApp.evictAccessToken(accessToken, type);
    }

    public UserView.Whole editUserProfile(UserPayload userDPO) {
        User user = userRepo.findOne(userDPO.id);
        user.edit(userDPO);
        userRepo.merge(user);
        return userService.buildViewOf(user).whole();
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

    public DownloadUrl changeUserOriginalAvatar(String userId, FileMetadataPayload newAvatar) {
        User user = userService.changeUserOriginalAvatar(userId, newAvatar.toDomainModel());
        return DownloadUrl.from(storageService.issueDownloadUrl(user.maxAvatar().get().key()));
    }

    public DownloadUrl changeUserHeaderPhoto(String userId, FileMetadataPayload newHeaderPhoto) {
        FileMetadata headerPhoto = userService.changeUserHeaderPhoto(userId, newHeaderPhoto.toDomainModel());
        return DownloadUrl.from(storageService.issueDownloadUrl(headerPhoto.key()));
    }

    public PersonalCenterVM getUserPersonalCenter(String aUserId, Optional<String> loginUserId) {
        UserView.Whole wholeUser = userService.getUserWithoutPwd(aUserId);
        Boolean isFollowedByLoginUser = Boolean.FALSE;
        if (loginUserId.isPresent()) {
            isFollowedByLoginUser = followRepo.getBy(loginUserId.get(), aUserId).isPresent();
        }
        return new PersonalCenterVM(wholeUser, isFollowedByLoginUser);
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

}