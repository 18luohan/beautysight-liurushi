/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.domain.service;

import com.beautysight.liurushi.community.domain.model.content.Author;

/**
 * @author chenlong
 * @since 1.0
 */
public interface AuthorService {

    Author getAuthorBy(String authorId);

}