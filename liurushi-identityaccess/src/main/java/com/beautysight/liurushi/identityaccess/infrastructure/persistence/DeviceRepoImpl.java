/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.infrastructure.persistence;

import com.beautysight.liurushi.common.persistence.mongo.AbstractMongoRepository;
import com.beautysight.liurushi.identityaccess.domain.model.Device;
import com.beautysight.liurushi.identityaccess.domain.model.User;
import com.beautysight.liurushi.identityaccess.domain.repo.DeviceRepo;
import com.google.common.base.Optional;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.springframework.stereotype.Repository;

/**
 * Here is Javadoc.
 * <p/>
 * Created by chenlong on 2015-05-13.
 *
 * @author chenlong
 * @since 1.0
 */
@Repository
public class DeviceRepoImpl extends AbstractMongoRepository<Device> implements DeviceRepo {

    @Override
    public Optional<Device> withImei(String imei) {
        return findOneBy(Conditions.of("imei", imei));
    }

    @Override
    public Device saveOrGet(Device aDevice) {
        Optional<Device> theDevice = withImei(aDevice.imei());
        if (theDevice.isPresent()) {
            return theDevice.get();
        }
        return save(aDevice);
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
