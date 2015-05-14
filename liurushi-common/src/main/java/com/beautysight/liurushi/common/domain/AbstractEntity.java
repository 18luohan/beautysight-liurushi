/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.common.domain;

import com.google.common.base.Preconditions;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Id;

import java.io.Serializable;
import java.util.Date;

/**
 * Here is Javadoc.
 * <p/>
 * Created by chenlong on 2015-05-08.
 *
 * @author chenlong
 * @since 1.0
 */
public abstract class AbstractEntity implements Serializable {

    private static final long serialVersionUID = -2361648049519441593L;

    @Id
    private ObjectId id;
    private Date createdAt = new Date();

    public ObjectId id() {
        return id;
    }

    public String idAsString() {
        Preconditions.checkState((id != null), "id is null");
        return id.toString();
    }

}
