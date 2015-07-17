/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.fundamental.infrastructure.storage;

import com.beautysight.liurushi.common.ex.StorageException;
import com.beautysight.liurushi.common.utils.Https;
import com.beautysight.liurushi.common.utils.Logs;
import com.beautysight.liurushi.fundamental.domain.storage.QiniuConfig;
import com.beautysight.liurushi.fundamental.domain.storage.ResourceInStorage;
import com.beautysight.liurushi.fundamental.domain.storage.StorageService;
import com.beautysight.liurushi.fundamental.domain.storage.UploadOptions;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Client;
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

/**
 * @author chenlong
 * @since 1.0
 */
@Service("storageService")
public class QiniuStorageService implements StorageService {

    private static final Logger logger = LoggerFactory.getLogger(QiniuStorageService.class);

    @Autowired
    private QiniuConfig qiniuConfig;

    private final UploadManager uploadManager = new UploadManager();
    private final Client client = new Client();

    private Auth auth;

    @PostConstruct
    public void init() {
        this.auth = Auth.create(qiniuConfig.accessKey, qiniuConfig.secretKey);
    }

    @Override
    public ResourceInStorage zoomImageTo(int expectedWidth, String imageKey) {
        String fileOps = String.format("imageMogr2/thumbnail/%dx", expectedWidth);
        String savedAsKey = imageKey + expectedWidth;
        int expiry = 0;
        final String zoomingImageUrl = this.issueDownloadUrlWithFileOps(imageKey, expiry, fileOps, savedAsKey);
        ResourceInStorage resource = QiniuServiceTemplate.request(
                String.format("download & %s", fileOps),
                new RequestExecutor() {
                    @Override
                    Response execute() throws QiniuException {
                        return client.get(zoomingImageUrl);
                    }
                });
        resource.setUrl(this.issueDownloadUrl(resource.getKey()));
        return resource;
    }

    @Override
    public ResourceInStorage blurImageAccordingTo(int radius, int sigma, String originalKey) {
        String fileOps = String.format("imageMogr2/blur/%dx%d", radius, sigma);
        String savedAsKey = originalKey + String.format("blur%dx%d", radius, sigma);
        int expiry = 0;
        final String gettingThumbnailInfoUrl = this.issueDownloadUrlWithFileOps(originalKey, expiry, fileOps, savedAsKey);
        ResourceInStorage resource = QiniuServiceTemplate.request(
                String.format("download & %s", fileOps),
                new RequestExecutor() {
                    @Override
                    Response execute() throws QiniuException {
                        return client.get(gettingThumbnailInfoUrl);
                    }
                });
        resource.setUrl(this.issueDownloadUrl(resource.getKey()));
        return resource;
    }

    @Override
    public String issueUploadToken(final UploadOptions options) {
        if (!options.isBucketGiven()) {
            options.scope(qiniuConfig.bucket);
        }

        return QiniuServiceTemplate.executeAuthService("issue upload token", new AuthServiceExecutor<String>() {
            @Override
            String execute() {
                return auth.uploadToken(options.scope().bucket(),
                        options.scope().key(),
                        options.deadline(),
                        options.toStringMap()
                );
            }
        });
    }

    @Override
    public String issueDownloadUrl(final String key) {
        return QiniuServiceTemplate.executeAuthService("issue download url", new AuthServiceExecutor<String>() {
            @Override
            String execute() {
                return auth.privateDownloadUrl(qiniuConfig.bucketUrl + "/" + key);
            }
        });
    }

    @Override
    public String issueDownloadUrlWithFileOps(final String key, final int expiry, final String fileOps, final String savedAsKey) {
        return QiniuServiceTemplate.executeAuthService(
                String.format("issue download url with %s", fileOps),
                new AuthServiceExecutor<String>() {
                    @Override
                    String execute() {
                        StringBuilder urlWithoutScheme = new StringBuilder();
                        urlWithoutScheme.append(qiniuConfig.bucketDomain).append("/").append(key);

                        StringBuilder queryString = new StringBuilder();
                        if (StringUtils.isNotBlank(fileOps)) {
                            queryString.append(fileOps);
                            if (StringUtils.isNotBlank(savedAsKey)) {
                                String scope = qiniuConfig.bucket + ":" + savedAsKey;
                                //Base64.encodeBase64URLSafeString(scope.getBytes("UTF-8"));
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
                    }
                });
    }

    public ResourceInStorage upload(final byte[] fileBytes, final String uploadToken) {
        return QiniuServiceTemplate.request("single direct upload", new RequestExecutor() {
            @Override
            public Response execute() throws QiniuException {
                return uploadManager.put(fileBytes, null, uploadToken);
            }
        });
    }

    public ResourceInStorage upload(final byte[] fileBytes, final String uploadToke, final StringMap params) {
        return QiniuServiceTemplate.request("single direct upload", new RequestExecutor() {
            @Override
            public Response execute() throws QiniuException {
                String key = null;
                String mime = null;
                boolean checkCrc = false;
                return uploadManager.put(fileBytes, key, uploadToke, params, mime, checkCrc);
            }
        });
    }

    private static class QiniuServiceTemplate {
        public static ResourceInStorage request(String apiDesc, RequestExecutor executor) {
            try {
                Response response = executor.execute();

                if (logger.isDebugEnabled()) {
                    Logs.debug(logger, "Request qiniu api: {}, response: {}, respBody: {}",
                            apiDesc, response.toString(), response.bodyString());
                }

                if (response.isOK()) {
                    return response.jsonToObject(ResourceInStorage.class);
                } else {
                    throw new StorageException("Qiniu responded with error, api: %s, error: %s",
                            apiDesc, response.error);
                }
            } catch (QiniuException ex) {
                String message = String.format(
                        "Qiniu responded with error, api: %s, error: %s",
                        apiDesc, ex.response.toString());
                throw logAndWrap(ex, message);
            } catch (Throwable ex) {
                String message = String.format("Unexpected error while call qiniu api: %s", apiDesc);
                throw logAndWrap(ex, message);
            }
        }

        public static <T> T executeAuthService(String authServiceDesc, AuthServiceExecutor<T> executor) {
            try {
                return executor.execute();
            } catch (Throwable ex) {
                String message = String.format("Unexpected error while qiniu auth service: %s", authServiceDesc);
                throw logAndWrap(ex, message);
            }
        }

        public static StorageException logAndWrap(Throwable ex, String message) {
            Logs.error(logger, ex, message);
            return new StorageException(message, ex);
        }
    }

    private static abstract class RequestExecutor {
        abstract Response execute() throws QiniuException;
    }

    private static abstract class AuthServiceExecutor<T> {
        abstract T execute();
    }

}