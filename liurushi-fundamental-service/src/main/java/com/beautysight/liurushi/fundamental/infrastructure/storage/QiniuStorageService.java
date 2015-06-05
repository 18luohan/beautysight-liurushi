/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.fundamental.infrastructure.storage;

import com.beautysight.liurushi.common.ex.ApplicationException;
import com.beautysight.liurushi.common.ex.CommonErrorId;
import com.beautysight.liurushi.common.utils.Https;
import com.beautysight.liurushi.fundamental.storage.domain.*;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import com.qiniu.util.UrlSafeBase64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;

/**
 * Here is Javadoc.
 * <p/>
 * Created by chenlong on 2015-05-18.
 *
 * @author chenlong
 * @since 1.0
 */
@Service
public class QiniuStorageService implements StorageService {

    private static final Logger logger = LoggerFactory.getLogger(QiniuStorageService.class);

    @Autowired
    private QiniuConfig qiniuConfig;

    private final UploadManager uploadManager = new UploadManager();
    private Auth auth;

    @PostConstruct
    public void init() {
        this.auth = Auth.create(qiniuConfig.accessKey, qiniuConfig.secretKey);
    }

    public ResourceInStorage zoomImageAccordingTo(int expectedWidth, String originalKey) {
        try {
            String instructions = String.format("imageMogr2/thumbnail/%dx", expectedWidth);
            String savedAsKey = originalKey + expectedWidth;
            int expiry = 0;
            String gettingThumbnailInfoUrl = this.getDownloadUrl(originalKey, expiry, instructions, savedAsKey);
            ResourceInStorage resource = Https.request(gettingThumbnailInfoUrl, ResourceInStorage.class);
            resource.url = this.getDownloadUrl(resource.key, expiry, null, null);
            return resource;
        } catch (IOException e) {
            throw new ApplicationException(CommonErrorId.internal_server_error, "Error while http", e);
        }
    }

    @Override
    public String getUploadToken(UploadOptions options) {
        try {
            if (!options.isBucketGiven()) {
                options.scope(qiniuConfig.bucket);
            }
            return auth.uploadToken(options.scope().bucket(),
                    options.scope().key(),
                    options.deadline(),
                    options.toStringMap()
            );
        } catch (Throwable e) {
            throw new ApplicationException(CommonErrorId.internal_server_error,
                    "Error while getting upload token from qiniu cloud storage", e);
        }
    }

    @Override
    public String getDownloadUrl(String key, int expiry, String instructions, String savedAsKey) {
        StringBuilder urlWithoutScheme = new StringBuilder();
        urlWithoutScheme.append(qiniuConfig.bucketDomain).append("/").append(key);

        StringBuilder queryString = new StringBuilder();
        if (StringUtils.isNotBlank(instructions)) {
            queryString.append(instructions);
            if (StringUtils.isNotBlank(savedAsKey)) {
                String scope = qiniuConfig.bucket + ":" + savedAsKey;
//                 Base64.encodeBase64URLSafeString(scope.getBytes("UTF-8"));
                queryString.append("|saveas/").append(UrlSafeBase64.encodeToString(scope));
            }
        }

        if (queryString.length() > 0) {
            urlWithoutScheme.append("?").append(Https.encodeWithUTF8(queryString.toString()));
            String sign = auth.sign(urlWithoutScheme.toString());
            urlWithoutScheme.append("/sign/").append(sign);
        }


        String url = qiniuConfig.bucketUrlScheme + urlWithoutScheme;
        if (expiry > 0) {
            return auth.privateDownloadUrl(url, expiry);
        }
        return auth.privateDownloadUrl(url);
        // TODO 考虑异常如何处理？
    }

    public UploadResult upload(final byte[] fileBytes, final String uploadToken) {
        return new UploadTemplate() {
            @Override
            public Response doUpload() throws QiniuException {
                return uploadManager.put(fileBytes, null, uploadToken);
            }
        }.upload();
    }

    public UploadResult upload(final byte[] fileBytes, final String uploadToke, final StringMap params) {
        return new UploadTemplate() {
            @Override
            public Response doUpload() throws QiniuException {
                String key = null;
                String mime = null;
                boolean checkCrc = false;
                return uploadManager.put(fileBytes, key, uploadToke, params, mime, checkCrc);
            }
        }.upload();
    }

    private static abstract class UploadTemplate {
        public UploadResult upload() {
            try {
                Response resp = doUpload();

                logger.debug("Response: {}", resp.toString());
                logger.debug("Response body: {}", resp.bodyString());

                if (resp.isOK()) {
                    return resp.jsonToObject(UploadResult.class);
                } else {
                    return UploadResult.failed(resp.error);
                }
            } catch (QiniuException e) {
                logger.error("Error response: {}", e.response.toString());

                try {
                    logger.error("Error response body: {}", e.response.bodyString());
                } catch (QiniuException e1) {
                    logger.error("Error while reading error response body", e);
                }

                return UploadResult.failed(e.response.error);
            } catch (Throwable e) {
                logger.error("Unexpected error while uploading file to Qiniu Cloud Storage", e);
                return UploadResult.failed(e.getMessage());
            }
        }

        public abstract Response doUpload() throws QiniuException;
    }

}
