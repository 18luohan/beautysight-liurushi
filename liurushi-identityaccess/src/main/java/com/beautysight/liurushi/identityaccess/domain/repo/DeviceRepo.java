/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.domain.repo;

import com.beautysight.liurushi.fundamental.infrastructure.persistence.mongo.MongoRepository;
import com.beautysight.liurushi.identityaccess.domain.model.Device;
import com.beautysight.liurushi.identityaccess.domain.model.User;
import com.google.common.base.Optional;

/**
 * Here is Javadoc.
 * <p/>
 * Created by chenlong on 2015-05-13.
 *
 * @author chenlong
 * @since 1.0
 */
public interface DeviceRepo extends MongoRepository<Device> {

    Optional<Device> withImei(String imei);

    Device saveOrGet(Device device);

    void addDeviceUser(String imei, User user);

}
