/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.test.mongo;

import com.mongodb.MongoClient;
import org.jongo.Jongo;
import org.springframework.util.Assert;

/**
 * Here is Javadoc.
 * <p/>
 * Created by chenlong on 2015-05-21.
 *
 * @author chenlong
 * @since 1.0
 */
public class JongoWrapper {

    private Jongo jongo;

    public JongoWrapper(MongoClient mongoClient, String dbName) {
        Assert.notNull(mongoClient, "mongoClient must not be null.");
        Assert.hasLength(dbName, "dbName must not be blank.");
        this.jongo = new Jongo(mongoClient.getDB(dbName));
    }

    public Jongo getJongo() {
        return this.jongo;
    }

}
