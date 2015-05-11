/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.domain.model;

/**
 * Here is Javadoc.
 * <p/>
 * Created by chenlong on 2015-05-08.
 *
 * @author chenlong
 * @since 1.0
 */
public class Device {

    private Type type;
    private String model;
    private String os;
    private String rom;
    private int ppi;
    private String imei;
    private String imsi;
    private String resolution;

    public enum Type {
        android, ios
    }

}
