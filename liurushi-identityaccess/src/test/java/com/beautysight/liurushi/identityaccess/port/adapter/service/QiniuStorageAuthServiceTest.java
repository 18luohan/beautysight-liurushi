/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.port.adapter.service;

import com.beautysight.liurushi.identityaccess.domain.model.UploadOptions;
import com.beautysight.liurushi.identityaccess.domain.model.UploadResult;
import com.qiniu.util.StringMap;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.UUID;

import static org.junit.Assert.*;


/**
 * Here is Javadoc.
 * <p/>
 * Created by chenlong on 2015-05-18.
 *
 * @author chenlong
 * @since 1.0
 */
public class QiniuStorageAuthServiceTest {

    private QiniuStorageAuthService storage = new QiniuStorageAuthService();

    private static final String TEST_BUCKET = "beautysight-test";
    private static final String BASE_DOWNLOAD_URL = "http://7xj3ch.com2.z0.glb.qiniucdn.com";

    @Test
    public void uploadToken() {
        UploadOptions uploadPolicy = uploadPolicyForTest().deadline(System.currentTimeMillis() / 1000 + 3600 * 12);
        String token = storage.getUploadToken(uploadPolicy);
        assertTrue(StringUtils.isNotBlank(token));
        System.out.println("token:" + token);
    }

    @Test
    public void downloadToken() throws Exception {
        String key = "FqdlHH3BabQMIHKxi9oMw5bdMNN3";
        int expiry = 0;
        String instructions = "/interlace/1";
        String savedAsKey = "FqdlHH3BabQMIHKxi9oMw5bdMNN3saveas";
        String downloadUrl = storage.getDownloadUrl(key, expiry, instructions, savedAsKey);
        System.out.println("url:" + downloadUrl);
        assertNotNull(downloadUrl);
    }

    @Test
    public void uploadTokenWithChecksum() throws IOException {
        UploadOptions uploadPolicy = uploadPolicyForTest()
                .checksum("pseudo-checksum-abcdefg-1000");
        String uploadToken = storage.getUploadToken(uploadPolicy);
        UploadResult result = storage.upload(Files.toBytes("images/jessy.jpg"), uploadToken);
        assertTrue(result.isSuccessful());
        assertTrue(StringUtils.isNotBlank(result.key));
        assertTrue(StringUtils.isNotBlank(result.hash));
    }

    @Test
    public void uploadWithDefaultPolicy() throws IOException {
        String uploadToken = storage.getUploadToken(uploadPolicyForTest());
        UploadResult result = storage.upload(Files.toBytes("images/jessy.jpg"), uploadToken);
        assertTrue(result.isSuccessful());
        assertTrue(StringUtils.isNotBlank(result.key));
        assertTrue(StringUtils.isNotBlank(result.hash));
    }

    @Test
    public void uploadWithCustomReturnBody() throws IOException {
        String returnBody = "{\"key\": $(key), \"hash\": $(etag), \"imageWidth\": $(imageInfo.width), \"imageHeight\": $(imageInfo.height)}";
        UploadOptions policy = uploadPolicyForTest().returnBody(returnBody);
        UploadResult result = storage.upload(Files.toBytes("images/jessy.jpg"), storage.getUploadToken(policy));

        assertTrue(result.isSuccessful());
        assertTrue(StringUtils.isNotBlank(result.key));
        assertTrue(StringUtils.isNotBlank(result.hash));
        BufferedImage origin = Files.from("images/jessy.jpg");
        assertEquals("image width not equal", origin.getWidth(), result.imageWidth);
        assertEquals("image width not equal", origin.getHeight(), result.imageHeight);
    }

    @Test
    public void uploadWithCustomVariableAndReturnBody() throws IOException {
        String returnBody = "{\"key\": $(key), \"hash\": $(etag), \"requestId\": $(x:requestId)}";
        UploadOptions policy = uploadPolicyForTest().returnBody(returnBody);

        String requestId = UUID.randomUUID().toString();
        StringMap params = new StringMap();
        params.put("x:requestId", requestId);

        UploadResult result = storage.upload(Files.toBytes("images/jessy.jpg"),
                storage.getUploadToken(policy), params);

        assertTrue(result.isSuccessful());
        assertTrue(StringUtils.isNotBlank(result.key));
        assertTrue(StringUtils.isNotBlank(result.hash));
        assertEquals("requestId", requestId, result.requestId);
    }

    private UploadOptions uploadPolicyForTest() {
        return UploadOptions.newInstance().scope(TEST_BUCKET);
    }

    private String sign(String url) {
        // TODO
        return null;
    }

}