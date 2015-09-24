/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.interfaces.identityaccess.facade.impl;

import com.beautysight.liurushi.common.utils.PreconditionUtils;
import com.beautysight.liurushi.identityaccess.domain.user.UserService;
import com.beautysight.liurushi.identityaccess.domain.user.UserView;
import com.beautysight.liurushi.interfaces.identityaccess.facade.UserFacade;
import com.beautysight.liurushi.interfaces.identityaccess.facade.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author chenlong
 * @since 1.0
 */
@Service
public class UserFacadeImpl implements UserFacade {

    @Autowired
    private UserService userService;

    @Override
    public UserDTO getLiteUserBy(String userId) {
        PreconditionUtils.checkRequired("userId", userId);
        UserView.Lite liteUser = userService.getLiteUser(userId);
        return translateToUserDTO(liteUser);
    }

    @Override
    public List<UserDTO> getLiteUsersBy(Collection<String> userIds) {
        PreconditionUtils.checkRequired("userIds", userIds);
        List<UserView.Lite> liteUsers = userService.getLiteUsers(userIds);
        return translateToUserDTOs(liteUsers);
    }

    @Override
    public void increaseWorkNumBy(int increment, String userId) {
        PreconditionUtils.checkRequired("userId", userId);
        userService.increaseWorksNumBy(increment, userId);
    }

    private UserDTO translateToUserDTO(UserView.Lite liteUser) {
        UserDTO userDTO = new UserDTO();
        userDTO.id = liteUser.getId();
        userDTO.nickname = liteUser.getNickname();
        userDTO.group = UserDTO.Group.valueOf(liteUser.getGroup().val());
        userDTO.commonAvatarUrl = liteUser.getCommonAvatarUrl();
        userDTO.maxAvatarUrl = liteUser.getMaxAvatarUrl();
        return userDTO;
    }

    private List<UserDTO> translateToUserDTOs(List<UserView.Lite> liteUsers) {
        List<UserDTO> userDTOs = new ArrayList<>(liteUsers.size());
        for (UserView.Lite liteUser : liteUsers) {
            userDTOs.add(translateToUserDTO(liteUser));
        }
        return userDTOs;
    }

}