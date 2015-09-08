/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.domain.service;

import com.beautysight.liurushi.community.domain.model.work.Author;

import java.util.Collection;
import java.util.List;

/**
 * @author chenlong
 * @since 1.0
 */
public interface AuthorService {

    Author getAuthorBy(String authorId);

    List<Author> getAuthorsBy(Collection<String> authorIds);

    void increaseWorkNumBy(int increment, String authorId);

}