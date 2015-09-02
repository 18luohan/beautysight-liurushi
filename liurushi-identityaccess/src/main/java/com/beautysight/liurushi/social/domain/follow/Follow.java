/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.social.domain.follow;

import com.beautysight.liurushi.common.domain.AbstractEntity;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;

/**
 * @author chenlong
 * @since 1.0
 */
@Entity(value = "follow", noClassnameStored = true)
public class Follow extends AbstractEntity {

    /**
     * 关注者，即粉丝
     */
    private ObjectId followerId;

    /**
     * 被关注的人
     */
    private ObjectId followingId;

    public Follow() {
    }

    public Follow(String followerId, String followingId) {
        this.followerId = new ObjectId(followerId);
        this.followingId = new ObjectId(followingId);
    }

    public String followerId() {
        return this.followerId.toHexString();
    }

    public String followingId() {
        return this.followingId.toHexString();
    }

}