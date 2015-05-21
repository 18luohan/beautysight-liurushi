/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.test.mongo;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;

/**
 * 表示一个要插入到给定集合中的文档。
 *
 * @author chenlong
 * @since 1.0
 */
public class JsonDoc {

    public static class Builder {

        private String collectionName;
        private StringBuilder jsonCollector = new StringBuilder();

        private boolean hasBuildingMaterial = true;

        public Builder collectionName(String collectionName) {
            if (StringUtils.isBlank(collectionName)) {
                return this;
            }
            this.collectionName = collectionName.trim();
            return this;
        }

        public Builder collectJsonFragment(String jsonFragment) {
            if (StringUtils.isBlank(jsonFragment)) {
                return this;
            }
            jsonCollector.append(jsonFragment.trim());
            return this;
        }

        public boolean isWorkFinished() {
            return (hasNoBuildingMaterial()
                    && isCollectionNamePresent()
                    && isJsonObjPresent());
        }

        public void setToNoBuildingMaterial() {
            this.hasBuildingMaterial = false;
        }

        public boolean buildingNotStart() {
            return (!isCollectionNamePresent()) && (!isJsonObjPresent());
        }

        public JsonDoc build() {
            Preconditions.checkState(isWorkFinished(), "this JsonData instance hasn't been complete:%s", jsonCollector);
            return new JsonDoc(this);
        }

        private boolean isCollectionNamePresent() {
            return StringUtils.isNotBlank(this.collectionName);
        }

        private boolean isJsonObjPresent() {
            return (jsonCollector.length() > 0);
        }

        private boolean hasNoBuildingMaterial() {
            return (!this.hasBuildingMaterial);
        }

    }

    public static Builder builder() {
        return new Builder();
    }

    public String getCollectionName() {
        return collectionName;
    }

    public String getJsonObjString() {
        return jsonObjString;
    }

    private JsonDoc(final Builder builder) {
        this.collectionName = builder.collectionName;
        this.jsonObjString = builder.jsonCollector.toString();
    }

    private String collectionName;
    private String jsonObjString;

}
