/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.persistence.mongo;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Id;

import java.io.Serializable;
import java.util.Date;

/**
 * @author chenlong
 * @since 1.0
 */
public abstract class MongoEntity implements Serializable {

    @Id
    protected ObjectId id;
    protected Date createdAt = new Date();
    protected Date modifiedAt;

    public void setId(String idString) {
        this.id = new ObjectId(idString);
    }

    public String id() {
        return id.toHexString();
    }

}
