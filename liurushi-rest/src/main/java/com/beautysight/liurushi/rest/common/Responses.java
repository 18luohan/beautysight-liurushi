/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.rest.common;

import com.beautysight.liurushi.common.ex.Error;
import com.beautysight.liurushi.common.utils.Jsons;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Here is Javadoc.
 * <p/>
 * Created by chenlong on 2015-05-11.
 *
 * @author chenlong
 * @since 1.0
 */
public class Responses {

    private static final Logger logger = LoggerFactory.getLogger(Responses.class);

    public static void setStatusAndWriteTo(HttpServletResponse response, Error.Id errorId, String message) {
        setStatus(response, errorId);
        writeTo(response, Jsons.toJsonString(Error.of(errorId, message)));
    }

    public static void setStatus(HttpServletResponse response, Error.Id errorId) {
        response.setStatus(ErrorIdToHttpStatusMapping.correspondingStatus(errorId).value());
    }

    public static void writeTo(HttpServletResponse response, String json) {
        if (StringUtils.isBlank(json)) {
            return;
        }

        PrintWriter writer = null;
        try {
            response.setCharacterEncoding("UTF-8");
            writer = response.getWriter();
            writer.write(json);
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException("I/O error while writing json to response", e);
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

}
