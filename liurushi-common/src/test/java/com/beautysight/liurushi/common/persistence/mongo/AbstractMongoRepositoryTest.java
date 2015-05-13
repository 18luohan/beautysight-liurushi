/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */
package com.beautysight.liurushi.common.persistence.mongo;

import org.junit.Test;
import org.springframework.data.domain.Sort;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Here is Javadoc.
 * <p/>
 * Created by chenlong on 2015-05-12.
 *
 * @author chenlong
 * @since 1.0
 */
public class AbstractMongoRepositoryTest {

    private AbstractMongoRepository<Void> repository = new AbstractMongoRepository<Void>() {
        @Override
        protected Class entityClass() {
            return Void.class;
        }
    };

    @Test
    public void test() {
        List<Sort.Order> orders = new ArrayList<Sort.Order>();
        orders.add(new Sort.Order(Sort.Direction.DESC, "score"));
        orders.add(new Sort.Order("name"));
        orders.add(new Sort.Order(Sort.Direction.ASC, "age"));
        String actual = repository.orderByFragment(new Sort(orders));
        String expected = "-score,name,age";
        assertEquals(expected, actual);
    }

}