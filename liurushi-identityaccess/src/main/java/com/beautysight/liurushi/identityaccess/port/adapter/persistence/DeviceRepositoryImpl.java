/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.port.adapter.persistence;

import com.beautysight.liurushi.common.persistence.mongo.AbstractMongoRepository;
import com.beautysight.liurushi.identityaccess.domain.model.Device;
import com.beautysight.liurushi.identityaccess.domain.model.User;
import com.beautysight.liurushi.identityaccess.domain.repo.DeviceRepository;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Here is Javadoc.
 * <p/>
 * Created by chenlong on 2015-05-13.
 *
 * @author chenlong
 * @since 1.0
 */
@Repository
public class DeviceRepositoryImpl extends AbstractMongoRepository<Device> implements DeviceRepository {

    @Override
    public List<Device> withImei(String imei) {
        return findBy(Conditions.of("imei", imei));
    }

    @Override
    public void addDeviceUser(String imei, User user) {
        Query<Device> updateQuery = newQuery(Conditions.of("imei", imei));
        UpdateOperations<Device> updateOps = newUpdateOps().add("userIds", user.id(), false);
        datastore.update(updateQuery, updateOps);
    }

    @Override
    protected Class<Device> entityClass() {
        return Device.class;
    }

}
