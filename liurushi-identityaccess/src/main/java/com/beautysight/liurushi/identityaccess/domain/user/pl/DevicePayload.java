/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.domain.user.pl;

import com.beautysight.liurushi.common.app.Payload;
import com.beautysight.liurushi.common.utils.PreconditionUtils;
import com.beautysight.liurushi.identityaccess.domain.user.Device;
import com.beautysight.liurushi.identityaccess.domain.user.Resolution;

/**
 * Here is Javadoc.
 * <p/>
 * Created by chenlong on 2015-05-08.
 *
 * @author chenlong
 * @since 1.0
 */
public class DevicePayload extends Payload {

    public Device.Type type;
    public String model;
    public String os;
    public String rom;
    public int ppi;
    public String imei;
    public String imsi;
    public Resolution resolution;

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

    public Device toDevice() {
        return new Device(type, model, os, rom, ppi, imei, imsi, resolution);
    }

}
