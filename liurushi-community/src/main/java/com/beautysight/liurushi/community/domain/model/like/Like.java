/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.community.domain.model.like;

import com.beautysight.liurushi.common.domain.AbstractEntity;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;

/**
 * @author chenlong
 * @since 1.0
 */
@Entity(value = "likes", noClassnameStored = true)
public class Like extends AbstractEntity {

    private ObjectId workId;
    private ObjectId userId;

    public Like() {
    }

    public Like(String workId, String userId) {
        this.workId = new ObjectId(workId);
        this.userId = new ObjectId(userId);
    }

    public ObjectId workMongoId() {
        return this.workId;
    }

    public String workId() {
        return this.workId.toHexString();
    }

    public ObjectId userMongoId() {
        return this.userId;
    }

    public String userId() {
        return this.userId.toHexString();
    }

}