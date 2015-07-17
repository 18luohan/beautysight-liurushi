/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.domain.service;

import com.beautysight.liurushi.community.domain.model.content.Author;
import com.beautysight.liurushi.interfaces.identityaccess.facade.dto.AccessTokenDTO;

/**
 * @author chenlong
 * @since 1.0
 */
public interface AuthorService {

    Author getAuthorBy(AccessTokenDTO accessToken);
    Author getAuthorBy(String authorId);

}