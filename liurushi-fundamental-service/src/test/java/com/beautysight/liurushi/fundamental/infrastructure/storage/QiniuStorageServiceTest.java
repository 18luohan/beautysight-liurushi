/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.fundamental.infrastructure.storage;

import com.beautysight.liurushi.common.utils.Https;
import com.beautysight.liurushi.fundamental.domain.storage.FileMetadata;
import com.beautysight.liurushi.fundamental.domain.storage.QiniuConfig;
import com.beautysight.liurushi.fundamental.domain.storage.UploadOptions;
import com.beautysight.liurushi.test.SpringBasedAppTest;
import com.beautysight.liurushi.test.utils.Files;
import com.qiniu.util.StringMap;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.util.UriUtils;

import java.io.IOException;
import java.util.UUID;

import static org.junit.Assert.*;

/**
 * @author chenlong
 * @since 1.0
 */
public class QiniuStorageServiceTest extends SpringBasedAppTest {

    @Autowired
    private QiniuStorageService storageService;
    @Autowired
    private QiniuConfig qiniuConfig;

    private static final String key = "Ftl9KyVXj1OPA_zEFa0Cz9B6KtTR";

    @Test
    public void issueUploadToken() {
        UploadOptions uploadPolicy = uploadPolicyForTest();
        String uploadToken = storageService.issueUploadToken(uploadPolicy);
        String uploadToken2 = storageService.issueUploadToken(uploadPolicy);
        System.out.println(">>>>>>>>>>>>>>>>>1:" + uploadToken);
        System.out.println(">>>>>>>>>>>>>>>>>2:" + uploadToken2);
        assertNotEquals(uploadToken, uploadToken2);
    }

    @Test
    public void issueUploadTokensWithDiffKeys() {
        UploadOptions uploadPolicy = uploadPolicyForTest();

        uploadPolicy.key("55baf21e8bb5e6731f94d564" + "01");
        String uploadToken = storageService.issueUploadToken(uploadPolicy);
        uploadPolicy.key("55bb05f58bb5e6731f94d56f" + "02");
        String uploadToken2 = storageService.issueUploadToken(uploadPolicy);

        System.out.println(">>>>>>>>>>>>>>>>>1:" + uploadToken);
        System.out.println(">>>>>>>>>>>>>>>>>2:" + uploadToken2);
        assertNotEquals(uploadToken, uploadToken2);
    }

    @Test
    public void issueDownloadUrl() {
        String result = storageService.issuePrivateDownloadUrl(UUID.randomUUID().toString());
        assertTrue(StringUtils.isNotBlank(result));
        System.out.println(result);
    }

    @Test
    public void uploadAvatar() throws IOException {
        String key = "hp_bee56d462d6e4c52ae75f60ad71caecb";

//        UploadOptions uploadPolicy = uploadPolicyForTest();
//        uploadPolicy.key(key);
//        String uploadToken = storageService.issueUploadToken(uploadPolicy);
//        System.out.println(">>>>>>>" + uploadToken);

        String uploadToken = "9AUEFpoKA-n2AZBWOwDrBvfFLQyqoG99S7-0HzjX:E-V_aZaWQGYI4DXiEDLXM19BYok=:eyJzY29wZSI6ImJlYXV0eXNpZ2h0LXRlc3Q6aHBfYmVlNTZkNDYyZDZlNGM1MmFlNzVmNjBhZDcxY2FlY2IiLCJjYWxsYmFja0ZldGNoS2V5IjowLCJpbnNlcnRPbmx5IjoxLCJkZXRlY3RNaW1lIjowLCJkZWFkbGluZSI6MTQ0NjcxODY2MH0=";

        String image = "heben.png"; // fecility heben
        FileMetadata result = storageService.upload(
                Files.readFileInClassPathAsBytes("images/" + image),
                key, uploadToken);
        System.out.println(image + ":" + result.key() + ", hash:" + result.hash());
    }

    @Test
    public void zoomImageTo() {
        int expectedWidth = 180;
        String zoomedImageKey = UUID.randomUUID().toString().replaceAll("-", "");
        FileMetadata resource = storageService.zoomImageTo(expectedWidth, key, zoomedImageKey);
        assertNotNull(resource);
//        System.out.println("error:" + resource.error + ", key:" + resource.key
//                + ", hash:" + resource.hash + ", url:" + resource.url);
    }

    @Test
    public void blurImage() {
        int radius = 30, segma = 20;
        String originalKey = "Ftl9KyVXj1OPA_zEFa0Cz9B6KtTR";
        FileMetadata resource = storageService.blurImageAccordingTo(radius, segma, originalKey);
        assertNotNull(resource);
//        System.out.println("error:" + resource.error + ", key:" + resource.key
//                + ", hash:" + resource.hash + ", url:" + resource.url);
    }

    @Test
    public void http() throws IOException {
        String url = "http://7xj3ch.com2.z0.glb.qiniucdn.com/Ftl9KyVXj1OPA_zEFa0Cz9B6KtTR?";
        String queryString = "imageMogr2/thumbnail/800x|saveas/YmVhdXR5c2lnaHQtdGVzdDpGdGw5S3lWWGoxT1BBX3pFRmEwQ3o5QjZLdFRSODAw/sign/9AUEFpoKA-n2AZBWOwDrBvfFLQyqoG99S7-0HzjX:6EdTzsHAZxHx6lNN5Ti9h2czo_I=&e=1433411717&token=9AUEFpoKA-n2AZBWOwDrBvfFLQyqoG99S7-0HzjX:j9STBsFgTVkElIJkhT1fuXlPn54=";
        url = url + UriUtils.encodeQuery(queryString, "UTF-8");
        FileMetadata resource = Https.request(url, FileMetadata.class);
//        System.out.println("error:" + resource.error + ", key:" + resource.key + ", hash:" + resource.hash);
    }

    @Test
    public void downloadToken() throws Exception {
        String key = "av_fd1687ecdc3b4f78b226182cfffc275f";
        int expiry = 0;
        String instructions = "imageView2/1/w/400/h/400/q/40";
        instructions = "imageMogr2/thumbnail/300x300";
        String savedAsKey = key + "x300";
        String downloadUrl = storageService.issueDownloadUrlWithFileOps(key, expiry, instructions, savedAsKey);
        System.out.println("savedAsKey:" + savedAsKey + ", url:" + downloadUrl);
        assertNotNull(downloadUrl);
    }

    @Test
    public void uploadTokenWithChecksum() throws IOException {
        UploadOptions uploadPolicy = uploadPolicyForTest()
                .checksum("pseudo-checksum-abcdefg-1000");
        String uploadToken = storageService.issueUploadToken(uploadPolicy);
        FileMetadata result = storageService.upload(
                Files.readFileInClassPathAsBytes("images/felicity.jpg"),
                FileMetadata.generateKey(FileMetadata.BizCategory.avatar), uploadToken);
    }

    @Test
    public void uploadWithDefaultPolicy() throws IOException {
        String uploadToken = storageService.issueUploadToken(uploadPolicyForTest());
        FileMetadata result = storageService.upload(Files.readFileInClassPathAsBytes("images/jessy.jpg"),
                FileMetadata.generateKey(FileMetadata.BizCategory.avatar), uploadToken);
    }

    @Test
    public void uploadWithCustomReturnBody() throws IOException {
        String returnBody = "{\"key\": $(key), \"hash\": $(etag), \"imageWidth\": $(imageInfo.width), \"imageHeight\": $(imageInfo.height)}";
        UploadOptions policy = uploadPolicyForTest().returnBody(returnBody);
        FileMetadata result = storageService.upload(Files.readFileInClassPathAsBytes("images/jessy.jpg"),
                FileMetadata.generateKey(FileMetadata.BizCategory.avatar), storageService.issueUploadToken(policy));

//        BufferedImage origin = Files.from("images/jessy.jpg");
//        assertEquals("image width not equal", origin.getWidth(), result.imageWidth);
//        assertEquals("image width not equal", origin.getHeight(), result.imageHeight);
    }

    @Test
    public void uploadWithCustomVariableAndReturnBody() throws IOException {
        String returnBody = "{\"key\": $(key), \"hash\": $(etag), \"requestId\": $(x:requestId)}";
        UploadOptions policy = uploadPolicyForTest().returnBody(returnBody);

        String requestId = UUID.randomUUID().toString();
        StringMap params = new StringMap();
        params.put("x:requestId", requestId);

        FileMetadata result = storageService.upload(Files.readFileInClassPathAsBytes("images/jessy.jpg"),
                storageService.issueUploadToken(policy), params);

//        assertEquals("requestId", requestId, result.requestId);
    }

    private UploadOptions uploadPolicyForTest() {
        return UploadOptions.newInstance(qiniuConfig);
    }

    private String sign(String url) {
        // TODO
        return null;
    }

}