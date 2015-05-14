/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */
package com.beautysight.liurushi.common.persistence.mongo;

import com.beautysight.liurushi.common.SpringBasedAppTest;
import com.beautysight.liurushi.common.domain.Product;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;

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
public class AbstractMongoRepositoryTest extends SpringBasedAppTest {

    @Autowired
    private ProductRepository repository;

    @Test
    public void save() {
        Product newProduct = new Product("mac-pro-15-1008", "1008", "mac pro", 15000, 13800);
        Product savedProduct = repository.save(newProduct);
        assertNotNull(savedProduct.id());
    }

    @Test
    public void orderingClause() {
        List<Sort.Order> orders = new ArrayList<Sort.Order>();
        orders.add(new Sort.Order(Sort.Direction.DESC, "score"));
        orders.add(new Sort.Order("name"));
        orders.add(new Sort.Order(Sort.Direction.ASC, "age"));
        String actual = repository.orderingClause(new Sort(orders));
        String expected = "-score, name, age";
        assertEquals(expected, actual);
    }

}