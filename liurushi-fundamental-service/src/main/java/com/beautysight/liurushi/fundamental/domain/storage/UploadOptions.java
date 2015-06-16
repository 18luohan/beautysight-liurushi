/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.fundamental.domain.storage;

import com.qiniu.util.StringMap;
import org.apache.commons.lang3.StringUtils;

/**
 * Key必须采用utf-8编码，使用非utf-8编码的资源名访问时会报错。<br/>
 * 1. callbackUrl与callbackBody配合使用。
 * 2. returnUrl与returnBody配合使用。
 * 3. callbackXXX与returnXXX不可混用。<p/>
 * 文件上传后的命名将遵循以下规则：
 * 1. 客户端已指定Key，以Key命名；
 * 2. 客户端未指定Key，上传策略中设置了saveKey，以saveKey的格式命名；
 * 3. 客户端未指定Key，上传策略中未设置saveKey，以文件hash（etag）命名。
 * <p/>
 * 更多请参考：http://developer.qiniu.com/docs/v6/api/reference/security/put-policy.html#fetch-key-explaination
 *
 * @author chenlong
 * @since 1.0
 */
public class UploadOptions {

    private Scope scope;
    private long deadline = unixTime() + 3600;
    private Switch insertOnly = Switch.disabled;
    private String endUser;

    /*
     * returnXxx属性通常用于HTML Form上传。
     * 在web端上传成功后，浏览器执行303跳转至上传端指定的<returnUrl>?upload_ret=<returnBody>。
     * returnBody就是上传端期望返回的数据，json格式，可包含七牛云的魔法变量和自定义变量。如果未
     * 设置returnUrl，则直接将returnBody返回给上传端。
     */

    private String returnUrl;
    private String returnBody;

    /*
     * callbackXxx属性用于上传成功后，云存储服务端回调应用服务器。
     * 应用服务端提供的回调API必须是通过公网可访问的，请求方式为Post，且其响应的Content-Type为
     * "application/json"。
     */

    /**
     * 回调Url。可同时指定多个，用英文封号(;)分隔。典型实例：http://<ip1>/callback;http://<ip2>/callback，
     * 并同时设置callbackHost，因为callbackUrl中主机地址用的是ip。在 callbackUrl 中使用 ip 的好处是减少了
     * 对 dns 解析的依赖，可改善回调的性能和稳定性。注意，必须以Post方式发送回调请求。
     * TODO 如果callbackUrl中使用的是域名，是不是就不需要设置callbackHost？
     */
    private String callbackUrl;
    /**
     * 用于回调的应用服务器主机地址。仅当设置了callbackUrl时，设置该属性才有意义。
     */
    private String callbackHost;
    /**
     * 描述回调时应用服务端期望云存储服务端传递给它的数据，支持七牛云的魔法变量和自定义变量。
     * 如果回调请求Content-Type为application/x-www-form-urlencoded，则应用服务端通过query string获取数据，
     * 相应地，也必须以合法的query string来描述期望的数据。
     * TODO 如果Content-Type为application/json，云存储服务端会传递json格式的数据吗？
     */
    private String callbackBody;
    /**
     * 回调请求的Content-Type。默认为application/x-www-form-urlencoded，也可设置为application/json。
     */
    private String callbackBodyType;
    /**
     * 是否启动通过回调应用服务端获取key。
     * 如果启用，则通过回调应用服务端获取key，并将其用作当前上传资源在云存储中的key。应用服务端返回给云存储的数据为：
     * { "key":<key>, "payload":<callback-result-json> }，
     * 云存储服务端获取该结果后，取key作为当前资源的key(或叫资源名称)，将payload返回给上传客户端。
     */
    private Switch callbackFetchKey = Switch.disabled;

    /**
     * 设置资源上传成功后要触发的预处理操作，以及处理结果的资源名称和存储空间。
     * 预处理操作表现形式为指令，每个指令是一个API规格字符串，多个指令用“;”分隔。
     */
    private String persistentOps;
    /**
     * 云存储使用方提供的用于接收预处理结果的url，必须公网可访问。Post请求，Content-Type为application/json。
     */
    private String persistentNotifyUrl;
    /**
     * 指定要将预处理任务添加至哪个工作队列
     */
    private String persistentPipeline;

    /**
     * 自定义资源key（或叫资源名称），仅在用户上传时没有主动指定key的时候起作用。
     */
    private String saveKey;

    /**
     * 上传文件的最大大小，单位为字节(byte)
     */
    private long fsizeLimit;

    /**
     * 是否开启云存储服务端MimeType自动侦测功能
     */
    private Switch detectMime = Switch.disabled;

    /**
     * 允许上传的文件类型
     */
    private String mimeLimit;
    private String checksum;

    private UploadOptions() {
    }

    public static UploadOptions newInstance() {
        return new UploadOptions();
    }

    public Scope scope() {
        return this.scope;
    }

    public long deadline() {
        return this.deadline;
    }

    public UploadOptions key(String key) {
        if (StringUtils.isBlank(key)) {
            return this;
        }
        return scope(null, key);
    }

    public UploadOptions scope(String bucket) {
        this.scope = new Scope(bucket);
        return this;
    }

    public UploadOptions scope(String bucket, String key) {
        this.scope = new Scope(bucket, key);
        return this;
    }

    public boolean isBucketGiven() {
        return (scope != null && StringUtils.isNotBlank(scope.bucket));
    }

    public UploadOptions deadline(long deadline) {
        this.deadline = deadline;
        return this;
    }

    public UploadOptions insertOnly(Switch state) {
        this.insertOnly = state;
        return this;
    }

    public UploadOptions endUser(String endUser) {
        this.endUser = endUser;
        return this;
    }

    public UploadOptions returnTo(String returnUrl, String returnBody) {
        this.returnUrl = returnUrl;
        this.returnBody = returnBody;
        return this;
    }

    public UploadOptions returnBody(String returnBody) {
        if (StringUtils.isBlank(returnBody)) {
            return this;
        }
        this.returnBody = returnBody;
        return this;
    }

    public UploadOptions callback(String url, String body, String bodyType) {
        return callback(url, null, body, bodyType, Switch.disabled);
    }

    public UploadOptions callback(String url, String body, String bodyType, Switch fetchKeyFromAppServer) {
        return callback(url, null, body, bodyType, fetchKeyFromAppServer);
    }

    public UploadOptions callback(String url, String host, String body, String bodyType) {
        return callback(url, host, body, bodyType, Switch.disabled);
    }

    public UploadOptions callback(String url, String host, String body, String bodyType, Switch fetchKeyFromAppServer) {
        this.callbackUrl = url;
        this.callbackHost = host;
        this.callbackBody = body;
        this.callbackBodyType = bodyType;
        this.callbackFetchKey = fetchKeyFromAppServer;
        return this;
    }

    public UploadOptions preprocessing(String preprocessingCommands, String notifyUrl, String jobQueue) {
        this.persistentOps = preprocessingCommands;
        this.persistentNotifyUrl = notifyUrl;
        this.persistentPipeline = jobQueue;
        return this;
    }

    public UploadOptions saveKey(String saveKey) {
        if (StringUtils.isBlank(saveKey)) {
            return this;
        }
        this.saveKey = saveKey;
        return this;
    }

    public UploadOptions fileSizeLimit(long maxSizeInBytesOfFile) {
        this.fsizeLimit = maxSizeInBytesOfFile;
        return this;
    }

    public UploadOptions autoDetectMime(Switch autoDetectMime) {
        this.detectMime = autoDetectMime;
        return this;
    }

    public UploadOptions mimeLimit(String allowedMime) {
        this.mimeLimit = allowedMime;
        return this;
    }

    public UploadOptions checksum(String checksum) {
        if (StringUtils.isBlank(checksum)) {
            return this;
        }
        this.checksum = checksum;
        return this;
    }

    public StringMap toStringMap() {
        StringMap stringMap = new StringMap();

        putFieldTo(stringMap, "scope", this.scope);
        putFieldTo(stringMap, "deadline", this.deadline);
        putFieldTo(stringMap, "insertOnly", this.insertOnly);
        putFieldTo(stringMap, "endUser", this.endUser);

        putFieldTo(stringMap, "returnUrl", this.returnUrl);
        putFieldTo(stringMap, "returnBody", this.returnBody);

        putFieldTo(stringMap, "callbackUrl", this.callbackUrl);
        putFieldTo(stringMap, "callbackHost", this.callbackHost);
        putFieldTo(stringMap, "callbackBody", this.callbackBody);
        putFieldTo(stringMap, "callbackBodyType", this.callbackBodyType);
        putFieldTo(stringMap, "callbackFetchKey", this.callbackFetchKey);

        putFieldTo(stringMap, "persistentOps", this.persistentOps);
        putFieldTo(stringMap, "persistentNotifyUrl", this.persistentNotifyUrl);
        putFieldTo(stringMap, "persistentPipeline", this.persistentPipeline);

        putFieldTo(stringMap, "saveKey", this.saveKey);
        putFieldTo(stringMap, "fsizeLimit", this.fsizeLimit);
        putFieldTo(stringMap, "detectMime", this.detectMime);
        putFieldTo(stringMap, "mimeLimit", this.mimeLimit);
        putFieldTo(stringMap, "checksum", this.checksum);

        return stringMap;
    }

    private void putFieldTo(StringMap map, String key, long fieldVal) {
        if (fieldVal > 0) {
            map.put(key, fieldVal);
        }
    }

    private void putFieldTo(StringMap map, String key, String fieldVal) {
        if (StringUtils.isNotBlank(fieldVal)) {
            map.put(key, fieldVal);
        }
    }

    private void putFieldTo(StringMap map, String key, Switch fieldVal) {
        if (fieldVal != null) {
            map.put(key, fieldVal.val());
        }
    }

    private void putFieldTo(StringMap map, String key, Scope scope) {
        if (scope != null) {
            map.put(key, scope.toString());
        }
    }

    public long unixTime() {
        return System.currentTimeMillis() / 1000;
    }

    public static class Scope {
        private String bucket;
        private String key;

        private Scope(String bucket) {
            this(bucket, null);
        }

        private Scope(String bucket, String key) {
            this.bucket = bucket;
            this.key = key;
        }


        public String bucket() {
            return this.bucket;
        }

        public String key() {
            return this.key;
        }

        @Override
        public String toString() {
            StringBuilder objString = new StringBuilder();
            if (StringUtils.isNotBlank(this.bucket)) {
                objString.append(this.bucket);
                if (StringUtils.isNotBlank(this.key)) {
                    objString.append(":").append(this.key);
                }
            }
            return objString.toString();
        }
    }

    private enum Switch {
        disabled(0), enabled(1);

        private int val;

        Switch(int val) {
            this.val = val;
        }

        public int val() {
            return this.val;
        }
    }

}

