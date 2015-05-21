# Storage Auth API

## Outline
* [upload_token](#upload_token)
* [download_url](#download_url)

## <a id="upload_token">upload_token</a>

#### Description
获取七牛云存储的上传凭证(upload_token)。

#### URL
```
POST /oauth/upload_token
```

#### Request Body

**实例**

```
{
    
        "key": "555947e9d4c6409386d9c6c4/20151012",
        "keyRxpr": "$(userId)/$(uploadTime)",
        "returnBody": {"key": $(key), "hash": $(etag), "imgWidth": $(imageInfo.width), "imgHeight": $(imageInfo.height)},
        "checksum": "555947e9d4c6409386d9c6c3"
}
```
**JSON属性说明**

 | Name | Type | Default | Required | Description | 
 |  ----  |  ----  |  ----  |  ----  |  ----  |
 | key | String | 有默认算法生成key | optional | 资源在云存储中对应的key。上传调用者可以在获取upload_token时就指定要使用key，也可以在实际上传时再指定。 | 
 | keyRxpr | String | N/A | optional | 用于生成key的自定义表达式。当未指定key时，用它生成key | 
 | returnBody | Json | {"key": $(key), "hash": $(etag)} | optional | 云存储返回给上传调用端的数据，该数据作为Http Response Body返回，格式为Json。默认值为{"key": $(key), "hash": $(etag)}，其中$(key)为变量取值表达式，key、etag是七牛云存储的内置变量，还可使用自定义变量。 | 
 | checksum | String | N/A | optional | 上传资源校验和。如果调用上传API时要求校验CRC，就需要指定该值。 | 
 
 *属性说明中未提及的属性即为可选的，且其取值没有约束。*


#### Response

**成功**

```
{
	"hash":"FqdlHH3BabQMIHKxi9oMw5bdMNN3",
	"key":"FqdlHH3BabQMIHKxi9oMw5bdMNN3"
}
```

**失败**

除了共用的错误Id（请参考Common Statuses and Responses）之外，目前该接口无特殊错误返回。

## <a id="download_url">download_url</a>

#### Description
根据给定的云存储中资源的key、过期时间，生成最终用来下载该资源的url。

#### URL
```
POST /oauth/download_url
```

#### Request Body

**实例**

```
{
    
        "key": "555947e9d4c6409386d9c6c4/20151012",
        "expiry": 3600,
        "instructions": "imageView/2/w/200/h/200|saveas/Ship-thumb-200.jpg"
    }
}
```
**JSON属性说明**

 | Name | Type | Default | Required | Description | 
 |  ----  |  ----  |  ----  |  ----  |  ----  |
 | key | String | N/A | yes | 上传成功后的返回数据之一。 | 
 | expiry | long | 3600s | optional | 该接口生成的下载url的有效期。 | 
 | instructions | 七牛命令管道格式，请参考七牛官方文档。 | N/A | optional | 实例给出的命令管道就是先对图片进行简单处理，并保存处理结果。 | 
 
 *属性说明中未提及的属性即为可选的，且其取值没有约束。*


#### Response

**成功**

```
{
	"url":"http://7xj3ch.com2.z0.glb.qiniucdn.com/FqdlHH3BabQMIHKxi9oMw5bdMNN3?e=1431925028&token=9AUEFpoKA-n2AZBWOwDrBvfFLQyqoG99S7-0HzjX:qZpQFEnM_wRC_-mF0bnMWZv6ySQ="
}
```

**失败**

除了共用的错误Id（请参考Common Statuses and Responses）之外，目前该接口无特殊错误返回。