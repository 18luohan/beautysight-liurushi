/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.common.persistence.mongo;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.springframework.beans.factory.config.AbstractFactoryBean;

/**
 * Here is Javadoc.
 * <p/>
 * Created by chenlong on 2015-05-12.
 *
 * @author chenlong
 * @since 1.0
 */
public class DataStoreFactoryBean extends AbstractFactoryBean<Datastore> {

    private Morphia morphia;
    private Mongo mongo;
    private String dbName;

    @Override
    public Class<?> getObjectType() {
        return Datastore.class;
    }

    @Override
    protected Datastore createInstance() throws Exception {
        if (!(mongo instanceof MongoClient)) {
            throw new RuntimeException("Expected MongoClient instance, but actual is Mongo instance");
        }

        MongoClient mongoClient = (MongoClient) mongo;
        Datastore dataStore = morphia.createDatastore(mongoClient, dbName);
        // TODO 是否需要在这里配置
//        dataStore.ensureCaps();
//        dataStore.ensureIndexes();
        return dataStore;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();
        if (mongo == null) {
            throw new IllegalStateException("mongo is not set");
        }
        if (morphia == null) {
            throw new IllegalStateException("morphia is not set");
        }
    }

    public void setMorphia(Morphia morphia) {
        this.morphia = morphia;
    }

    public void setMongo(Mongo mongo) {
        this.mongo = mongo;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

}