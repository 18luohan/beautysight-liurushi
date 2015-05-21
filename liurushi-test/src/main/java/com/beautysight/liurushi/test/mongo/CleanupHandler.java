/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.test.mongo;

import org.jongo.Jongo;
import org.jongo.MongoCollection;

/**
 * Here is Javadoc.
 * <p/>
 * Created by chenlong on 2015-05-20.
 *
 * @author chenlong
 * @since 1.0
 */
public class CleanupHandler extends MongoAnnotationHandler<Cleanup> {

    @Override
    protected String[] getValueOf(Cleanup annotation) {
        return annotation.value();
    }

    @Override
    protected Class<Cleanup> annotationType() {
        return Cleanup.class;
    }

    @Override
    protected String scriptSuffix(Cleanup annotation) {
        return ".c" + super.scriptFileExt();
    }

    @Override
    protected void onEach(JsonDoc jsonDoc, Jongo jongo) {
        MongoCollection collection = jongo.getCollection(jsonDoc.getCollectionName());
        collection.remove(jsonDoc.getJsonObjString());
    }

}
