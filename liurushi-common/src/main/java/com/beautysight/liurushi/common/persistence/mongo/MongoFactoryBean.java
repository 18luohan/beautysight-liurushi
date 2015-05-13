/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.common.persistence.mongo;


import java.util.ArrayList;
import java.util.List;

import com.mongodb.*;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.config.AbstractFactoryBean;

/**
 * Here is Javadoc.
 * <p/>
 * Created by chenlong on 2015-05-12.
 *
 * @author chenlong
 * @since 1.0
 */
//public class MongoFactoryBean extends AbstractFactoryBean<MongoClient> {
//
//    private List<ServerAddress> replicaSetSeeds = new ArrayList<ServerAddress>();
//    private MongoClientOptions clientOptions;
//
//    @Override
//    public Class<?> getObjectType() {
//        return Mongo.class;
//    }
//
//    @Override
//    protected Mongo createInstance() throws Exception {
//        if (replicaSetSeeds.size() > 0) {
//            if (clientOptions != null) {
//                return new Mongo(replicaSetSeeds, clientOptions);
//            }
//            return new Mongo(replicaSetSeeds);
//        }
//        return new Mongo();
//    }
//
//    public void setMultiAddress(String[] serverAddresses) {
//        replSeeds(serverAddresses);
//    }
//
//    private void replSeeds(String... serverAddresses) {
//        try {
//            replicaSetSeeds.clear();
//            for (String addr : serverAddresses) {
//                String[] a = addr.split(":");
//                String host = a[0];
//                if (a.length > 2) {
//                    throw new IllegalArgumentException("Invalid Server Address : " + addr);
//                } else if (a.length == 2) {
//                    replicaSetSeeds.add(new ServerAddress(host, Integer.parseInt(a[1])));
//                } else {
//                    replicaSetSeeds.add(new ServerAddress(host));
//                }
//            }
//        } catch (Exception e) {
//            throw new BeanCreationException("Error while creating replicaSetAddresses", e);
//        }
//    }
//
//    public void setAddress(String serverAddress) {
//        replSeeds(serverAddress);
//    }
//
//}
