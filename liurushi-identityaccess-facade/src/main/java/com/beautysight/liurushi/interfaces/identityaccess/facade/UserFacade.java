/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.interfaces.identityaccess.facade;

import com.beautysight.liurushi.interfaces.identityaccess.facade.dto.UserDTO;

import java.util.Collection;
import java.util.List;

/**
 * This facade shields the domain layer - model, services, repositories -
 * from concerns about such things as the user interface and remoting.
 *
 * @author chenlong
 * @since 1.0
 */
public interface UserFacade {

    UserDTO getLiteUserBy(String userId);

    List<UserDTO> getLiteUsersBy(Collection<String> userIds);

    void increaseWorkNumBy(int increment, String userId);

}