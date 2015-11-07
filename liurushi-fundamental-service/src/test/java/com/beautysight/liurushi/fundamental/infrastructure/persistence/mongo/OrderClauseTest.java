/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.fundamental.infrastructure.persistence.mongo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.*;

/**
 * @author chenlong
 * @since 1.0
 */
@RunWith(JUnit4.class)
public class OrderClauseTest {

    private OrderClause orderClause = new OrderClause().addFixedAsc("score")
            .addInvertibleDesc("name")
            .addFixedDesc("subject")
            .addInvertibleAsc("no");

    @Test
    public void orderClause() {
        assertEquals("score,-name,-subject,no", orderClause.orderClause());
    }

    @Test
    public void inverseOrderClause() {
        assertEquals("score,name,-subject,-no", orderClause.inverseOrderClause());
    }

}