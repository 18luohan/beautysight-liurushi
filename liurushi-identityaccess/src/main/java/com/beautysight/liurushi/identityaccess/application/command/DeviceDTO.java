/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.application.command;

import com.beautysight.liurushi.common.utils.PreconditionUtils;
import com.beautysight.liurushi.identityaccess.domain.model.Device;
import com.beautysight.liurushi.identityaccess.domain.model.Resolution;

/**
 * Here is Javadoc.
 * <p/>
 * Created by chenlong on 2015-05-08.
 *
 * @author chenlong
 * @since 1.0
 */
public class DeviceDTO {

    private Device.Type type;
    private String model;
    private String os;
    private String rom;
    private int ppi;
    private String imei;
    private String imsi;
    private Resolution resolution;

    public void validate() {
        PreconditionUtils.checkRequired("device.type", type);
        PreconditionUtils.checkRequired("device.model", model);
        PreconditionUtils.checkRequired("device.os", os);
        PreconditionUtils.checkRequired("device.rom", rom);
        PreconditionUtils.checkGreaterThanZero("device.ppi", ppi);
        PreconditionUtils.checkRequired("device.imei", imei);
        PreconditionUtils.checkRequired("device.resolution", resolution);
        resolution.validate();
    }

}
