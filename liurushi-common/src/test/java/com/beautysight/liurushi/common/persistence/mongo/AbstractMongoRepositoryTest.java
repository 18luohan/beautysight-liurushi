/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */
package com.beautysight.liurushi.common.persistence.mongo;

import com.beautysight.liurushi.common.domain.Category;
import com.beautysight.liurushi.common.domain.Product;
import com.beautysight.liurushi.test.SpringBasedAppTest;
import com.beautysight.liurushi.test.mongo.Cleanup;
import com.beautysight.liurushi.test.mongo.Prepare;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
    private ProductRepository productRepo;

    @Autowired
    private CategoryRepository categoryRepo;

    @Test
    public void save() {
        Category category = new Category("Portable Computer", "No description");
        categoryRepo.save(category);
        Product newProduct = new Product("mac-pro-15-1008", "1008", "mac pro", 15000, 13800, category);
        Product savedProduct = productRepo.save(newProduct);
        assertNotNull(savedProduct.id());
    }

    @Test
    @Prepare()
    @Cleanup()
    public void find() {
        long count = productRepo.count();
        assertEquals(3, count);
        Iterator<Product> products = productRepo.findAll().iterator();
        while (products.hasNext()) {
            Product product = products.next();
            assertNotNull(product);
        }
    }

    @Test
    public void delete() {
        productRepo.deleteAll();
    }

    @Test
    public void orderingClause() {
        List<Sort.Order> orders = new ArrayList<Sort.Order>();
        orders.add(new Sort.Order(Sort.Direction.DESC, "score"));
        orders.add(new Sort.Order("name"));
        orders.add(new Sort.Order(Sort.Direction.ASC, "age"));
        String actual = productRepo.orderingClause(new Sort(orders));
        String expected = "-score, name, age";
        assertEquals(expected, actual);
    }

}