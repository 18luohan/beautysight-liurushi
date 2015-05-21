/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.common.utils;

import com.beautysight.liurushi.common.ex.JsonHandlingException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.util.Date;

/**
 * Here is Javadoc.
 * <p/>
 * Created by chenlong on 2015-05-11.
 *
 * @author chenlong
 * @since 1.0
 */
public class Jsons {

    private static final ObjectMapper mapper = new CustomObjectMapper();

    /*static {
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'"));
    }*/

    public static String toJsonString(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new JsonHandlingException(String.format("Convert %s obj to json string", obj.getClass()), e);
        }
    }

    public static class CustomObjectMapper extends ObjectMapper {

        public CustomObjectMapper() {
            super();

            final SimpleModule customModule = new SimpleModule("CustomSimpleModule", Version.unknownVersion());
            customModule.addSerializer(Date.class, new CustomDateSerializer());
            this.registerModule(customModule);

            this.configure(MapperFeature.AUTO_DETECT_FIELDS, true);
            this.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            //this.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            /*this.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>() {
                @Override
                public void serialize(
                        Object value,
                        JsonGenerator gen,
                        SerializerProvider sp) throws IOException, JsonProcessingException {
                    gen.writeString("");
                }
            });*/
        }

    }

}
