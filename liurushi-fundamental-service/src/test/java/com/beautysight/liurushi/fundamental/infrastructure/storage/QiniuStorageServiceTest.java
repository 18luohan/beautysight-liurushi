/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.fundamental.infrastructure.storage;

import com.beautysight.liurushi.common.utils.Https;
import com.beautysight.liurushi.fundamental.domain.storage.ResourceInStorage;
import com.beautysight.liurushi.fundamental.domain.storage.UploadOptions;
import com.beautysight.liurushi.fundamental.domain.storage.UploadResult;
import com.beautysight.liurushi.test.SpringBasedAppTest;
import com.beautysight.liurushi.test.utils.Files;
import com.qiniu.util.StringMap;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.util.UriUtils;

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
public class QiniuStorageServiceTest extends SpringBasedAppTest {

    @Autowired
    private QiniuStorageService storageService;

    @Test
    public void uploadAvatar() throws IOException {
        UploadOptions uploadPolicy = uploadPolicyForTest();
        String uploadToken = storageService.getUploadToken(uploadPolicy);
        UploadResult result = storageService.upload(Files.toBytes("images/avatar1.png"), uploadToken);
        assertTrue(result.isSuccessful());
        assertTrue(StringUtils.isNotBlank(result.key));
        assertTrue(StringUtils.isNotBlank(result.hash));
        System.out.println("key:" + result.key + ", hash:" + result.hash);
    }

    @Test
    public void zoomImageAccordingTo() {
        int expectedWidth = 200;
        String originalKey = "Ftl9KyVXj1OPA_zEFa0Cz9B6KtTR";
        ResourceInStorage resource = storageService.zoomImageAccordingTo(expectedWidth, originalKey);
        assertNotNull(resource);
        System.out.println("error:" + resource.error + ", key:" + resource.key
                + ", hash:" + resource.hash + ", url:" + resource.url);
    }

    @Test
    public void http() throws IOException {
        String url = "http://7xj3ch.com2.z0.glb.qiniucdn.com/Ftl9KyVXj1OPA_zEFa0Cz9B6KtTR?";
        String queryString = "imageMogr2/thumbnail/800x|saveas/YmVhdXR5c2lnaHQtdGVzdDpGdGw5S3lWWGoxT1BBX3pFRmEwQ3o5QjZLdFRSODAw/sign/9AUEFpoKA-n2AZBWOwDrBvfFLQyqoG99S7-0HzjX:6EdTzsHAZxHx6lNN5Ti9h2czo_I=&e=1433411717&token=9AUEFpoKA-n2AZBWOwDrBvfFLQyqoG99S7-0HzjX:j9STBsFgTVkElIJkhT1fuXlPn54=";
        url = url + UriUtils.encodeQuery(queryString, "UTF-8");
        ResourceInStorage resource = Https.request(url, ResourceInStorage.class);
        System.out.println("error:" + resource.error + ", key:" + resource.key + ", hash:" + resource.hash);
    }

    @Test
    public void uploadToken() {
        UploadOptions uploadPolicy = uploadPolicyForTest().deadline(System.currentTimeMillis() / 1000 + 3600 * 12);
        String token = storageService.getUploadToken(uploadPolicy);
        assertTrue(StringUtils.isNotBlank(token));
        System.out.println("token:" + token);
    }

    @Test
    public void downloadToken() throws Exception {
        String key = "FgwkvStH0913xPKZiRgJxvG_9SXM";
//        key = "d45ba164-a6ce-43e0-9ed2-659c003ce162";
        int expiry = 0;
        String instructions = "imageView2/1/w/400/h/400/q/40";
//        instructions = null;
        String savedAsKey = UUID.randomUUID().toString();
//        savedAsKey = null;
        String downloadUrl = storageService.getDownloadUrl(key, expiry, instructions, savedAsKey);
        System.out.println("savedAsKey:" + savedAsKey + ", url:" + downloadUrl);
        assertNotNull(downloadUrl);
    }

    @Test
    public void uploadTokenWithChecksum() throws IOException {
        UploadOptions uploadPolicy = uploadPolicyForTest()
                .checksum("pseudo-checksum-abcdefg-1000");
        String uploadToken = storageService.getUploadToken(uploadPolicy);
        UploadResult result = storageService.upload(Files.toBytes("images/felicity.jpg"), uploadToken);
        assertTrue(result.isSuccessful());
        assertTrue(StringUtils.isNotBlank(result.key));
        assertTrue(StringUtils.isNotBlank(result.hash));
    }

    @Test
    public void uploadWithDefaultPolicy() throws IOException {
        String uploadToken = storageService.getUploadToken(uploadPolicyForTest());
        UploadResult result = storageService.upload(Files.toBytes("images/jessy.jpg"), uploadToken);
        assertTrue(result.isSuccessful());
        assertTrue(StringUtils.isNotBlank(result.key));
        assertTrue(StringUtils.isNotBlank(result.hash));
    }

    @Test
    public void uploadWithCustomReturnBody() throws IOException {
        String returnBody = "{\"key\": $(key), \"hash\": $(etag), \"imageWidth\": $(imageInfo.width), \"imageHeight\": $(imageInfo.height)}";
        UploadOptions policy = uploadPolicyForTest().returnBody(returnBody);
        UploadResult result = storageService.upload(Files.toBytes("images/jessy.jpg"), storageService.getUploadToken(policy));

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

        UploadResult result = storageService.upload(Files.toBytes("images/jessy.jpg"),
                storageService.getUploadToken(policy), params);

        assertTrue(result.isSuccessful());
        assertTrue(StringUtils.isNotBlank(result.key));
        assertTrue(StringUtils.isNotBlank(result.hash));
        assertEquals("requestId", requestId, result.requestId);
    }

    private UploadOptions uploadPolicyForTest() {
        return UploadOptions.newInstance();
    }

    private String sign(String url) {
        // TODO
        return null;
    }

}