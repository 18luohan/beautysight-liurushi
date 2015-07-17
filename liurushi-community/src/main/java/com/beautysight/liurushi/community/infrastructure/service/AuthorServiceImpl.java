/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.infrastructure.service;

import com.beautysight.liurushi.community.domain.model.content.Author;
import com.beautysight.liurushi.community.domain.service.AuthorService;
import com.beautysight.liurushi.interfaces.identityaccess.facade.UserFacade;
import com.beautysight.liurushi.interfaces.identityaccess.facade.dto.AccessTokenDTO;
import com.beautysight.liurushi.interfaces.identityaccess.facade.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author chenlong
 * @since 1.0
 */
@Service
public class AuthorServiceImpl implements AuthorService {

    @Autowired
    private UserFacade userFacade;

    @Override
    public Author getAuthorBy(AccessTokenDTO accessToken) {
        UserDTO userDTO = userFacade.findUserByAccessToken(accessToken);
        return new Author(userDTO.id, userDTO.nickname, null, null);
    }

    @Override
    public Author getAuthorBy(String authorId) {
        UserDTO userDTO = userFacade.getUserBy(authorId);
        return new Author(userDTO.id,
                userDTO.nickname,
                userDTO.originalAvatarUrl,
                userDTO.maxAvatarUrl);
    }

}
