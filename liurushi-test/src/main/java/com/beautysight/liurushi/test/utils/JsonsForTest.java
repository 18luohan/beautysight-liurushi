/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.test.utils;

import com.beautysight.liurushi.common.utils.Jsons;

import java.io.File;

/**
 * @author chenlong
 * @since 1.0
 */
public class JsonsForTest {

    public static <T> T readJsonFileInSameDirWithTestClassAsModel(Class<?> testClass, String jsonFile, Class<T> modelClass) {
        File theJsonFile = Files.fileInSameDirWith(testClass, jsonFile);
        return Jsons.toObject(theJsonFile, modelClass);
    }

}
