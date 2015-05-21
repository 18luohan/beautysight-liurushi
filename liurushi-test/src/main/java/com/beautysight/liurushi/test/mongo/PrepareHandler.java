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
public class PrepareHandler extends MongoAnnotationHandler<Prepare> {

    @Override
    protected String[] getValueOf(Prepare annotation) {
        return annotation.value();
    }

    @Override
    protected Class<Prepare> annotationType() {
        return Prepare.class;
    }

    @Override
    protected void onEach(JsonDoc jsonDoc, Jongo jongo) {
        MongoCollection collection = jongo.getCollection(jsonDoc.getCollectionName());
        collection.insert(jsonDoc.getJsonObjString());
    }

}
