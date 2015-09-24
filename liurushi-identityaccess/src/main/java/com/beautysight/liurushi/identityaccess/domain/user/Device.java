/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.domain.user;

import com.beautysight.liurushi.common.domain.AbstractEntity;
import com.google.common.collect.Lists;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Reference;

import java.util.List;

/**
 * @author chenlong
 * @since 1.0
 */
@Entity(value = "devices", noClassnameStored = true)
public class Device extends AbstractEntity {

    private Type type;
    private String model;
    private String os;
    private String rom;
    private int ppi;
    private String imei;
    private String imsi;
    private Resolution resolution;

    @Reference(value = "userIds", lazy = true, idOnly = true)
    private List<User> users;

    private Device() {
    }

    public Device(Type type, String model, String os, String rom, int ppi, String imei, String imsi, Resolution resolution) {
        this.type = type;
        this.model = model;
        this.os = os;
        this.rom = rom;
        this.ppi = ppi;
        this.imei = imei;
        this.imsi = imsi;
        this.resolution = resolution;
    }

    /**
     * 为当前设备添加使用者
     */
    public void addUser(User aUser) {
        if (users == null) {
            users = Lists.newArrayList();
        }
        users.add(aUser);
    }

    public String imei() {
        return this.imei;
    }

    public Resolution resolution() {
        return this.resolution;
    }

    public enum Type {
        android, ios
    }

}
