/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.fundamental.infrastructure.persistence.mongo;

import org.springframework.stereotype.Repository;

/**
 * Here is Javadoc.
 * <p/>
 * Created by chenlong on 2015-05-18.
 *
 * @author chenlong
 * @since 1.0
 */
@Repository
public class CategoryRepository extends AbstractMongoRepository<Category> {

    @Override
    protected Class<Category> entityClass() {
        return Category.class;
    }

}
