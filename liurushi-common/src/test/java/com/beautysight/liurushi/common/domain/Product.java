/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.common.domain;

import org.mongodb.morphia.annotations.Entity;

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

    public Product(String slug, String sku, String name, int retail, int sale) {
        this.slug = slug;
        this.sku = sku;
        this.name = name;
        this.pricing = new Pricing(retail, sale);
    }

    private static class Pricing {
        private int retail;
        private int sale;

        private Pricing(int retail, int sale) {
            this.retail = retail;
            this.sale = sale;
        }
    }

}