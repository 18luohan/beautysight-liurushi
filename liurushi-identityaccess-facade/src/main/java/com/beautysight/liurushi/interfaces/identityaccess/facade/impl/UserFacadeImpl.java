/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.interfaces.identityaccess.facade.impl;

import com.beautysight.liurushi.common.utils.PreconditionUtils;
import com.beautysight.liurushi.fundamental.domain.storage.StorageService;
import com.beautysight.liurushi.identityaccess.app.OAuthApp;
import com.beautysight.liurushi.identityaccess.domain.model.User;
import com.beautysight.liurushi.identityaccess.domain.repo.UserRepo;
import com.beautysight.liurushi.interfaces.identityaccess.facade.UserFacade;
import com.beautysight.liurushi.interfaces.identityaccess.facade.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author chenlong
 * @since 1.0
 */
@Service
public class UserFacadeImpl implements UserFacade {

    @Autowired
    private OAuthApp oAuthApp;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private StorageService storageService;

    @Override
    public UserDTO getUserBy(String userId) {
        PreconditionUtils.checkRequired("userId", userId);
        User user = userRepo.findOne(userId);

        UserDTO result = new UserDTO();
        result.id = user.id().toString();
        result.nickname = user.nickname();
        result.group = UserDTO.Group.valueOf(user.group().toString());

        if (user.hasAvatar()) {
            result.originalAvatarUrl = storageService.issueDownloadUrl(user.originalAvatar().get().key());
            result.maxAvatarUrl = storageService.issueDownloadUrl(user.maxAvatar().get().key());
        }

        return result;
    }

}