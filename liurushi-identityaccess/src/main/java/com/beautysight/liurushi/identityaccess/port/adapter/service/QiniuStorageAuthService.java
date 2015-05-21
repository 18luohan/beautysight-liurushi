/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.identityaccess.port.adapter.service;

import com.beautysight.liurushi.common.ex.ApplicationException;
import com.beautysight.liurushi.common.ex.CommonErrorId;
import com.beautysight.liurushi.identityaccess.common.Environment;
import com.beautysight.liurushi.identityaccess.domain.model.UploadOptions;
import com.beautysight.liurushi.identityaccess.domain.model.UploadResult;
import com.beautysight.liurushi.identityaccess.domain.service.StorageAuthService;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import com.qiniu.util.UrlSafeBase64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Here is Javadoc.
 * <p/>
 * Created by chenlong on 2015-05-18.
 *
 * @author chenlong
 * @since 1.0
 */
@Service
public class QiniuStorageAuthService implements StorageAuthService {

    private static final Logger logger = LoggerFactory.getLogger(QiniuStorageAuthService.class);

    private String ACCESS_KEY = "9AUEFpoKA-n2AZBWOwDrBvfFLQyqoG99S7-0HzjX";
    private String SECRET_KEY = "xppuUVH10EK8C-uhFEyNAmYsByF27zHSxg7SSI-n";

    private final UploadManager uploadManager = new UploadManager();
    private final Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
    private static final String DOMAIN_OF_BUCKET = "7xj3ch.com2.z0.glb.qiniucdn.com";
    private static final String DOWNLOAD_URL_SCHEME = "http://";
    private static final String BASE_DOWNLOAD_URL = DOWNLOAD_URL_SCHEME + DOMAIN_OF_BUCKET;

    @Override
    public String getUploadToken(UploadOptions options) {
        try {
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
        urlWithoutScheme.append(DOMAIN_OF_BUCKET).append("/").append(key);

        StringBuilder queryString = new StringBuilder();
        if (StringUtils.isNotBlank(instructions)) {
            queryString.append(instructions);
            if (StringUtils.isNotBlank(savedAsKey)) {
                String scope = Environment.bucket + ":" + savedAsKey;
                // Base64.encodeBase64URLSafeString(scope.getBytes("UTF-8"));
                queryString.append("|saveas/").append(UrlSafeBase64.encodeToString(scope));
            }
        }

        if (queryString.length() > 0) {
            urlWithoutScheme.append("?").append(queryString);
            String sign = auth.sign(urlWithoutScheme.toString());
            urlWithoutScheme.append("/sign/").append(sign);
        }


        String url = DOWNLOAD_URL_SCHEME + urlWithoutScheme;
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
