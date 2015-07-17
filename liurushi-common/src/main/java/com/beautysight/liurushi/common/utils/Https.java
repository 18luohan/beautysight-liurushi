/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.common.utils;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.util.UriUtils;

import java.io.IOException;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Http client utils
 *
 * @author chenlong
 * @since 1.0
 */
public final class Https {

    private static final Logger logger = LoggerFactory.getLogger(Https.class);

    private static final OkHttpClient httpClient = new OkHttpClient();

    public static String encodeWithUTF8(String s) {
        return encode(s, "UTF-8");
    }

    public static String encode(String s, String enc) {
        try {
            return URLEncoder.encode(s, enc);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static <T> T request(String url, Class<T> type) throws IOException {
        // Create request for remote resource.
        Request request = new Request.Builder()
                .url(url)
                .build();

        // Execute the request and retrieve the response.
        Response response = httpClient.newCall(request).execute();

        // Deserialize HTTP response to concrete type.
        Reader bodyReader = response.body().charStream();
        return Jsons.toObject(bodyReader, type);
    }

    public final static void main(String[] args) throws Exception {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            String query = UriUtils.encodeQuery("imageView2/1/w/200/h/200|imageMogr2/crop/50x50&attname=down3.jpg", "UTF-8");
            System.out.println(query);
            HttpGet httpget = new HttpGet("http://newdocs.qiniudn.com/gogopher.jpg?" + query);
            System.out.println("Executing request " + httpget.getRequestLine());

            // Create a custom response handler
            ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

                @Override
                public String handleResponse(
                        final HttpResponse response) throws ClientProtocolException, IOException {
                    int status = response.getStatusLine().getStatusCode();
                    if (status >= 200 && status < 300) {
                        HttpEntity entity = response.getEntity();
                        return entity != null ? EntityUtils.toString(entity) : null;
                    } else {
                        throw new ClientProtocolException("Unexpected response status: " + status);
                    }
                }

            };
            String responseBody = httpclient.execute(httpget, responseHandler);
            System.out.println("----------------------------------------");
            System.out.println(responseBody);
        } finally {
            httpclient.close();
        }
        System.out.println(URLEncoder.encode("|", "UTF-8"));
    }

}
