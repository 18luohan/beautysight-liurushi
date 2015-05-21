/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.common.domain;

import org.mongodb.morphia.annotations.Entity;

/**
 * Here is Javadoc.
 * <p/>
 * Created by chenlong on 2015-05-18.
 *
 * @author chenlong
 * @since 1.0
 */
@Entity("categories")
public class Category extends AbstractEntity {
    private String name;
    private String description;

    private Category() {}

    public Category(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
