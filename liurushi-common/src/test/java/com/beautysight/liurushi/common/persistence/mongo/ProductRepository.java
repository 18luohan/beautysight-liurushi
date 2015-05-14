/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.common.persistence.mongo;

import com.beautysight.liurushi.common.domain.Product;
import org.springframework.stereotype.Repository;

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

    @Override
    protected Class<Product> entityClass() {
        return Product.class;
    }

}
