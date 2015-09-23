/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.infrastructure.service;

import com.beautysight.liurushi.community.domain.work.Author;
import com.beautysight.liurushi.community.domain.service.AuthorService;
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
public class AuthorServiceImpl implements AuthorService {

    @Autowired
    private UserFacade userFacade;

    @Override
    public Author getAuthorBy(String authorId) {
        UserDTO userDTO = userFacade.getLiteUserBy(authorId);
        return translateToAuthor(userDTO);
    }

    @Override
    public List<Author> getAuthorsBy(Collection<String> authorIds) {
        return translateToAuthors(userFacade.getLiteUsersBy(authorIds));
    }

    @Override
    public void increaseWorkNumBy(int increment, String authorId) {
        userFacade.increaseWorkNumBy(increment, authorId);
    }

    private List<Author> translateToAuthors(List<UserDTO> userDTOs) {
        List<Author> authors = new ArrayList<>(userDTOs.size());
        for (UserDTO userDTO : userDTOs) {
            authors.add(translateToAuthor(userDTO));
        }
        return authors;
    }

    private Author translateToAuthor(UserDTO userDTO) {
        return new Author(userDTO.id,
                userDTO.nickname,
                userDTO.group.toString(),
                userDTO.maxAvatarUrl);
    }
}
