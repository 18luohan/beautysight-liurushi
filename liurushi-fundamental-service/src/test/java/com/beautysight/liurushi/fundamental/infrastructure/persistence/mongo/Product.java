/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.fundamental.infrastructure.persistence.mongo;

import com.beautysight.liurushi.common.domain.AbstractEntity;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Reference;

/**
 * Here is Javadoc.
 * <p/>
 * Created by chenlong on 2015-05-13.
 *
 * @author chenlong
 * @since 1.0
 */
@Entity(value = "products", noClassnameStored = true)
public class Product extends AbstractEntity {

    private String slug;
    private String sku;
    private String name;
    private Pricing pricing;

    @Reference(value = "category_id", idOnly = true, lazy = false)
    private Category category;

    private Product() {
    }

    public Product(String slug, String sku, String name, int retail, int sale, Category category) {
        this.slug = slug;
        this.sku = sku;
        this.name = name;
        this.pricing = new Pricing(retail, sale);
        this.category = category;
    }

    private static class Pricing {
        private int retail;
        private int sale;

        private Pricing() {}

        private Pricing(int retail, int sale) {
            this.retail = retail;
            this.sale = sale;
        }
    }

}