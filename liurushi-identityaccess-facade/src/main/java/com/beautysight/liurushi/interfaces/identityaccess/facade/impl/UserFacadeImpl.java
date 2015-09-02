/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.interfaces.identityaccess.facade.impl;

import com.beautysight.liurushi.common.utils.PreconditionUtils;
import com.beautysight.liurushi.identityaccess.domain.dpo.UserDPO;
import com.beautysight.liurushi.identityaccess.domain.service.UserService;
import com.beautysight.liurushi.interfaces.identityaccess.facade.UserFacade;
import com.beautysight.liurushi.interfaces.identityaccess.facade.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        UserDPO liteUser = userService.getLiteUser(userId);
        return translateToUserDTO(liteUser);
    }

    public List<UserDTO> getLiteUsersBy(List<String> userIds) {
        PreconditionUtils.checkRequired("userIds", userIds);
        List<UserDPO> liteUsers = userService.getLiteUsersWithStats(userIds);
        return translateToUserDTOs(liteUsers);
    }

    private List<UserDTO> translateToUserDTOs(List<UserDPO> liteUsers) {
        List<UserDTO> userDTOs = new ArrayList<>(liteUsers.size());
        for (UserDPO user : liteUsers) {
            userDTOs.add(translateToUserDTO(user));
        }
        return userDTOs;
    }

    private UserDTO translateToUserDTO(UserDPO liteUser) {
        UserDTO userDTO = new UserDTO();
        userDTO.id = liteUser.id;
        userDTO.nickname = liteUser.nickname;
        userDTO.group = UserDTO.Group.valueOf(liteUser.group.toString());
        userDTO.maxAvatarUrl = liteUser.maxAvatarUrl;
        return userDTO;
    }

}