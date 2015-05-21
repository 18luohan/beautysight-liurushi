/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.common.persistence.mongo;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.mongodb.morphia.Morphia;
import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * Here is Javadoc.
 * <p/>
 * Created by chenlong on 2015-05-12.
 *
 * @author chenlong
 * @since 1.0
 */
public class MorphiaFactoryBean extends AbstractFactoryBean<Morphia> {

    private List<String> mapClasses;
    private List<String> mapPackages;
    private boolean ignoreInvalidClasses;
    private boolean useBulkWriteOperations = false;

    @Override
    public Class<?> getObjectType() {
        return Morphia.class;
    }

    @Override
    protected Morphia createInstance() throws Exception {
        Morphia morphia = new Morphia();
        morphia.setUseBulkWriteOperations(useBulkWriteOperations);
        if (!CollectionUtils.isEmpty(mapClasses)) {
            for (String entityClass : mapClasses) {
                morphia.map(Class.forName(entityClass));
            }
        }
        if (!CollectionUtils.isEmpty(mapPackages)) {
            for (String packageName : mapPackages) {
                morphia.mapPackage(packageName, ignoreInvalidClasses);
            }
        }
        return morphia;
    }

    public void setMapClasses(List<String> mapClasses) {
        this.mapClasses = mapClasses;
    }

    public void setMapPackages(List<String> mapPackages) {
        this.mapPackages = mapPackages;
    }

    public void setEntityPackages(String entityPackages) {
        Preconditions.checkArgument(StringUtils.isNotBlank(entityPackages), "entityPackages is blank");
        String[] packages = entityPackages.split(",");
        this.mapPackages = Lists.newArrayList(packages);
    }

    public void setIgnoreInvalidClasses(boolean ignoreInvalidClasses) {
        this.ignoreInvalidClasses = ignoreInvalidClasses;
    }

    public void setUseBulkWriteOperations(boolean useBulkWriteOperations) {
        this.useBulkWriteOperations = useBulkWriteOperations;
    }

}

