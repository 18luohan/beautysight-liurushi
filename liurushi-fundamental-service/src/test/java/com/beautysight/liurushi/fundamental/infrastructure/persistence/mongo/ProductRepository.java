/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.fundamental.infrastructure.persistence.mongo;

import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Here is Javadoc.
 * <p/>
 * Created by chenlong on 2015-05-13.
 *
 * @author chenlong
 * @since 1.0
 */
@Repository
public class ProductRepository extends AbstractMongoRepository<Product> {

    public List<Product> findProductsBy(Category category) {
        return findBy(Conditions.newWithEqual("category_id", category.id()));
    }

    @Override
    protected Class<Product> entityClass() {
        return Product.class;
    }

}
