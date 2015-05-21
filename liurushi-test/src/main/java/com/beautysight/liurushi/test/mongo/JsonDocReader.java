/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.test.mongo;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Here is Javadoc.
 * <p/>
 * Created by chenlong on 2015-05-18.
 *
 * @author chenlong
 * @since 1.0
 */
public class JsonDocReader {

    private static final Logger logger = LoggerFactory.getLogger(JsonDocReader.class);

    private static final String COLLECTION_DECLARING_KEYWORDS = "// #";


    public static List<JsonDoc> read(File jsonFile) throws IOException {
        List<JsonDoc> jsonDocs = new ArrayList<JsonDoc>();
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(jsonFile), "UTF-8"));

            String line;
            JsonDoc.Builder previousBuilder = JsonDoc.builder();

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (StringUtils.isBlank(line)) {
                    continue;
                }

                if (line.startsWith(COLLECTION_DECLARING_KEYWORDS)) {
                    previousBuilder.setToNoBuildingMaterial();
                    if (previousBuilder.buildingNotStart()) {
                        previousBuilder.collectionName(collectionName(line));
                    } else if (previousBuilder.isWorkFinished()) {
                        jsonDocs.add(previousBuilder.build());
                        previousBuilder = JsonDoc.builder();
                    } else {
                        previousBuilder = JsonDoc.builder().collectionName(collectionName(line));
                    }
                } else if (line.startsWith("//")) {
                    continue;
                } else {
                    previousBuilder.collectJsonFragment(line);
                }
            }

            previousBuilder.setToNoBuildingMaterial();
            if (previousBuilder.isWorkFinished()) {
                jsonDocs.add(previousBuilder.build());
            }
        } catch (IOException e) {
            throw e;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    logger.error("Error while closing file: " + jsonFile, e);
                }
            }
        }

        return jsonDocs;
    }

    public static List<JsonDoc> read(List<File> jsonFiles) throws IOException {
        List<JsonDoc> jsonDocs = new ArrayList<JsonDoc>();
        if (CollectionUtils.isEmpty(jsonFiles)) {
            return jsonDocs;
        }

        for (File jsonFile : jsonFiles) {
            jsonDocs.addAll(read(jsonFile));
        }

        return jsonDocs;
    }

    private static String collectionName(String collectionDeclaringLine) {
        return collectionDeclaringLine.substring(COLLECTION_DECLARING_KEYWORDS.length()).trim();
    }

}
