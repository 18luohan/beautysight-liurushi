/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.fundamental.storage.domain;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Here is Javadoc.
 * <p/>
 * Created by chenlong on 2015-06-05.
 *
 * @author chenlong
 * @since 1.0
 */
@Component
public final class QiniuConfig {

    @Value("${qiniu.storage.bucket}")
    public String bucket;

    @Value("${qiniu.storage.bucket.domain}")
    public String bucketDomain;

    @Value("${qiniu.storage.bucket.url.scheme}")
    public String bucketUrlScheme;

    @Value("${qiniu.storage.access.key}")
    public String accessKey;

    @Value("${qiniu.storage.secret.key}")
    public String secretKey;

}
