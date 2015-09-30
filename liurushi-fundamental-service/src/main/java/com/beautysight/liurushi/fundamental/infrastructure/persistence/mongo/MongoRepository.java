/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.fundamental.infrastructure.persistence.mongo;

import org.bson.types.ObjectId;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author chenlong
 * @since 1.0
 */
public interface MongoRepository<T> extends PagingAndSortingRepository<T, ObjectId> {

    T findOne(String id);

    void merge(T entity);

    int remove(String id);

}
