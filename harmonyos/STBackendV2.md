---
title: STBackendV2
language_tabs:
  - shell: Shell
  - http: HTTP
  - javascript: JavaScript
  - ruby: Ruby
  - python: Python
  - php: PHP
  - java: Java
  - go: Go
toc_footers: []
includes: []
search: true
code_clipboard: true
highlight_theme: darkula
headingLevel: 2
generator: "@tarslib/widdershins v4.0.30"

---

# STBackendV2

Base URLs:

# Authentication

* API Key (apikey-undefined-X-QW-Api-Key)
    - Parameter Name: **X-QW-Api-Key**, in: header. 

# User

## GET GetInfo

GET /user/info

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|satoken|header|string| 否 |none|

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

## POST UserLogin

POST /user/login

> Body 请求参数

```json
{
    "phone":"13800138007",
    "password":"138007"
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|body|body|object| 否 |none|
|» phone|body|string| 是 |none|
|» password|body|string| 是 |none|

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

## POST UserRegister

POST /user/register

> Body 请求参数

```json
{
    "username":"testUser1",
    "password":"123456",
    "phone":"120-120-1120",
    "inviteCode":"fH18u020"
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|body|body|object| 否 |none|
|» username|body|string| 是 |none|
|» password|body|string| 是 |none|
|» phone|body|string| 是 |none|
|» inviteCode|body|string| 是 |none|

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

## GET UserList

GET /user/list

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|satoken|header|string| 否 |none|

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

## GET GetCurrentUser

GET /user

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|satoken|header|string| 否 |none|

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

## PUT UserUpdate

PUT /user

> Body 请求参数

```json
{
    "username":"testUser1-up-1",
    "rawPassword":"1234567",
    "newPassword":"123456"
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|body|body|object| 否 |none|
|» username|body|string| 是 |none|
|» rawPassword|body|string| 是 |none|
|» newPassword|body|string| 是 |none|

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

## POST UserLogout

POST /user/logout

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

## POST UserLeaveEnterprise

POST /user/leave

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

## POST UserJoinEnterprise

POST /user/join

> Body 请求参数

```json
{
    "invitedCode":"*!nsu+xG"
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|body|body|object| 否 |none|
|» invitedCode|body|string| 是 |none|

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

## PUT ResetPass

PUT /user/reset

> Body 请求参数

```json
{
    "phone":"120-120-1120",
    "resetKey":"w2Q8nZk3pL6xT1vB9sR4eY7a"
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|body|body|object| 否 |none|
|» phone|body|string| 是 |none|
|» resetKey|body|string| 是 |none|

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

# Enterprise

## POST CreateEnterprise

POST /enterprise

> Body 请求参数

```json
{
    "name":"test-enterprise",
    "address":"test-address",
    "contactPerson":"testUser",
    "contactPhone":"178-888-9999"
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|body|body|object| 否 |none|
|» name|body|string| 是 |none|
|» address|body|string| 是 |none|
|» contactPerson|body|string| 是 |none|
|» contactPhone|body|string| 是 |none|

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

## GET GetEnterpriseInfo

GET /enterprise

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

## PUT UpdateEnterprise

PUT /enterprise

> Body 请求参数

```json
{
    "name":"test-enterprise-up",
    "address":"test-address-up",
    "contactPerson":"testUser1-up-1",
    "contactPhone":"120-120-1120"
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|body|body|object| 否 |none|
|» name|body|string| 是 |none|
|» address|body|string| 是 |none|
|» contactPerson|body|string| 是 |none|
|» contactPhone|body|string| 是 |none|

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

## GET GetEnterpriseFarm

GET /enterprise/farms

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

## GET GenInviitedCode

GET /enterprise/invitedCode

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

## GET GetCount

GET /enterprise/count

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

# Farm

## POST CreateFarm

POST /farm

> Body 请求参数

```json
{
    "name":"test-farm",
    "address":"test-address",
    "supervisorId": "11"
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|body|body|object| 否 |none|
|» name|body|string| 是 |none|
|» address|body|string| 是 |none|
|» supervisorId|body|string| 是 |none|

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

## GET GetFamrInfo

GET /farm

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|farmId|query|string| 是 |none|

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

## PUT UpdateFarm

PUT /farm

> Body 请求参数

```json
{
    "id":1,
    "name":"test-farm-update",
    "address":"test-address-update",
    "supervisorId": 13
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|body|body|object| 否 |none|
|» id|body|integer| 是 |none|
|» name|body|string| 是 |none|
|» address|body|string| 是 |none|
|» supervisorId|body|integer| 是 |none|

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

## DELETE DeleteFarm

DELETE /farm

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|farmId|query|string| 是 |none|

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

## GET GetFarmSites

GET /farm/sites

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|farmId|query|string| 是 |none|

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

# FarmSite

## POST CreateSite

POST /farmSite

> Body 请求参数

```json
{
    "farmId":1,
    "name":"test-up-site",
    "sum":100
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|body|body|object| 否 |none|
|» farmId|body|integer| 是 |none|
|» name|body|string| 是 |none|
|» sum|body|integer| 是 |none|

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

## GET GetSite

GET /farmSite

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|farmSiteId|query|string| 是 |none|

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

## PUT UpdateSite

PUT /farmSite

> Body 请求参数

```json
{
    "farmId":1,
    "siteId":4,
    "name":"test-up-site-up",
    "sum":1000,
    "properties": {
        "time": "20250829",
        "days": "今天情人节，我陪代码过"
    }
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|body|body|object| 否 |none|
|» farmId|body|integer| 是 |none|
|» siteId|body|integer| 是 |none|
|» name|body|string| 是 |none|
|» sum|body|integer| 是 |none|
|» properties|body|object| 是 |none|
|»» time|body|string| 是 |none|
|»» days|body|string| 是 |none|

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

## DELETE DelSite

DELETE /farmSite

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|farmSiteId|query|string| 是 |none|

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

## GET GetSiteDevice

GET /farmSite/devices

> Body 请求参数

```json
{}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|farmSiteId|query|string| 是 |none|
|body|body|object| 否 |none|

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

## GET camera

GET /farmSite/camera

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|farmSiteId|query|string| 是 |none|

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

# DeviceType

## POST CreateType

POST /device-type

> Body 请求参数

```json
{
    "typeId": 3,
    "typeName":"测试设备类型"
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|body|body|object| 否 |none|
|» typeId|body|integer| 是 |none|
|» typeName|body|string| 是 |none|

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

## GET TypeList

GET /device-type

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

## DELETE TypeDel

DELETE /device-type

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|typeId|query|string| 是 |none|

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

## PUT UpdateType

PUT /device-type

> Body 请求参数

```json
{
    "typeId":3,
    "typeName":"test-type-update"
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|body|body|object| 否 |none|
|» typeId|body|integer| 是 |none|
|» typeName|body|string| 是 |none|

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

# Device

## POST CreateDevice

POST /device

> Body 请求参数

```json
{
    "name":"cam-南区1-5",
    "siteId":8,
    "mac":"12345.12345.12345.12345",
    "type":1,
    "pushName":""
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|body|body|object| 否 |none|
|» name|body|string| 是 |none|
|» siteId|body|integer| 是 |none|
|» mac|body|string| 是 |none|
|» type|body|integer| 是 |none|
|» pushName|body|string| 是 |none|

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

## GET GetDevice

GET /device

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|deviceId|query|string| 是 |none|

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

## DELETE DelDevice

DELETE /device

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|deviceId|query|string| 是 |none|

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

## PUT UpdateDevice

PUT /device

> Body 请求参数

```json
{
    "deviceId":19,
    "name":"cam-南区1-4",
    "pushName":"cam-north1--4",
    "siteId":8,
    "type":1,
    "status":1,
    "properties":{
        "add":"追加内容"
    }
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|body|body|object| 否 |none|
|» deviceId|body|integer| 是 |none|
|» name|body|string| 是 |none|
|» pushName|body|string| 是 |none|
|» siteId|body|integer| 是 |none|
|» type|body|integer| 是 |none|
|» status|body|integer| 是 |none|
|» properties|body|object| 是 |none|
|»» add|body|string| 是 |none|

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

## GET DevicePage

GET /device/page

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|pageNum|query|string| 是 |none|
|name|query|string| 否 |none|
|pageSize|query|string| 是 |none|

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

# DeviceData

## POST CreateData

POST /device-data

> Body 请求参数

```json
{
    "deviceId": 40,
    "data": {
        "info": "PM2.5:7 PM10:9 humidity:57.2 temperature:27.3 Tvoc:34 c02:1688 methanal:0.01",
        "mac": "00:0:a4:0f:fe:ef"
    }
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|body|body|object| 否 |none|
|» deviceId|body|integer| 是 |none|
|» data|body|object| 是 |none|
|»» info|body|string| 是 |none|
|»» mac|body|string| 是 |none|

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

## GET GetData

GET /device-data

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|pageNum|query|string| 是 |none|
|pageSize|query|string| 是 |none|
|startTime|query|string| 否 |none|
|deviceId|query|string| 是 |none|

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

## GET GetDataById

GET /device-data/allById

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|deviceId|query|string| 是 |none|

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

## GET GetLatestData

GET /device-data/latest

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|deviceId|query|string| 是 |none|

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

# SSE

## GET SSEConnect

GET /sse/connect

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|userId|query|string| 是 |none|
|enterpriseId|query|string| 是 |none|
|clientType|query|string| 是 |none|

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

## GET SSECount

GET /sse/count

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

## POST SSETest

POST /sse/test

> Body 请求参数

```json
{
    "targetType": "web",
    "eventName": "test",
    "data": {
        "code": 501,
        "msg": "南区养殖点异常"
    },
    "enterpriseId": 5
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|body|body|object| 否 |none|
|» targetType|body|string| 是 |none|
|» eventName|body|string| 是 |none|
|» data|body|object| 是 |none|
|»» code|body|integer| 是 |none|
|»» msg|body|string| 是 |none|
|» enterpriseId|body|integer| 是 |none|

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

# Notification

## POST CreateNoti

POST /notification

> Body 请求参数

```json
{
    "enterpriseId":4,
    "title":"test-create-3",
    "content":"test-create-content-3",
    "type":1
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|body|body|object| 否 |none|
|» enterpriseId|body|integer| 是 |none|
|» title|body|string| 是 |none|
|» content|body|string| 是 |none|
|» type|body|integer| 是 |none|

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

## DELETE DelNoti

DELETE /notification

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|notificationId|query|string| 是 |none|

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

## GET NotDelAndRead

GET /notification/allNotDeleteAndNotRead

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

## GET NotDel

GET /notification/allNotDelete

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

## PUT ReadOne

PUT /notification/readOne

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|notificationId|query|string| 是 |none|

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

## PUT ReadAll

PUT /notification/readAll

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

# Order

## POST CreateOrder

POST /order

> Body 请求参数

```json
{
    "title": "测试工单251207-工单系统支持媒体文件",
    "description": "测试工单描述-工单支持媒体文件",
    "status": 1
}

```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|body|body|object| 否 |none|
|» title|body|string| 是 |none|
|» description|body|string| 是 |none|
|» status|body|integer| 是 |none|

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

## GET GetById

GET /order

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|id|query|string| 是 |none|

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

## PUT Update

PUT /order

> Body 请求参数

```json
{
    "id":32,
    "title":"测试工单251207-工单系统支持媒体文件-update",
    "description":"测试工单描述-工单支持媒体文件-update",
    "status":1,
    "solution": "测试解决方案",
    "solutionUrls": [
        "order/image/xiaohei2512071027.jpg"
    ],
    "situation": "测试巡查情况",
    "situationUrls": [
        "order/image/xiaohei2512071027.jpg"
    ]
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|body|body|object| 否 |none|
|» id|body|integer| 是 |none|
|» title|body|string| 是 |none|
|» description|body|string| 是 |none|
|» status|body|integer| 是 |none|
|» solution|body|string| 是 |none|
|» solutionUrls|body|[string]| 是 |none|
|» situation|body|string| 是 |none|
|» situationUrls|body|[string]| 是 |none|

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

## DELETE Del

DELETE /order

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|id|query|string| 是 |none|

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

## GET GetAll

GET /order/list

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

## POST MarkCompl

POST /order/complete

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|id|query|string| 是 |none|

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

## POST QueryOrder

POST /order/query

> Body 请求参数

```json
{
    "pageNum":1,
    "pageSize":2,
    "title":"",
    "description":"测试",
    "status":null,
    "creatorId": 1,
    "solvedId": 40
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|body|body|object| 否 |none|
|» pageNum|body|integer| 是 |none|
|» pageSize|body|integer| 是 |none|
|» title|body|string| 是 |none|
|» description|body|string| 是 |none|
|» status|body|null| 是 |none|
|» creatorId|body|integer| 是 |none|
|» solvedId|body|integer| 是 |none|

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

## POST Dispatch

POST /order/dispatch

> Body 请求参数

```json
{
    "orderId":18,
    "userId":15
}

```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|body|body|object| 否 |none|
|» orderId|body|integer| 是 |none|
|» userId|body|integer| 是 |none|

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

## POST AcceptOrder

POST /order/accept

> Body 请求参数

```json
{
    "orderId":18
}

```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|body|body|object| 否 |none|
|» orderId|body|integer| 是 |none|

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

## GET GetAcceptOrder

GET /order/user

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|userId|query|string| 是 |none|

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

# Warn

## POST CreateWarn

POST /warn

> Body 请求参数

```json
{
    "enterpriseId":5,
    "type":101,
    "description":"2025.11.16警告测试"
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|body|body|object| 否 |none|
|» enterpriseId|body|integer| 是 |none|
|» type|body|integer| 是 |none|
|» description|body|string| 是 |none|

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

# InoutData

## GET getData

GET /inout/yearData

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|year|query|string| 是 |none|

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

## GET getFarmSiteData

GET /inout/yearSiteData

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|year|query|string| 是 |none|
|siteId|query|string| 是 |none|

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

# Admin

## GET Check

GET /admin/check

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

## GET getUserWithRole

GET /admin/users

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

## DELETE KickUser

DELETE /admin/users/27

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

# OBS

## POST GenSignatureUrls

POST /oss/signature

> Body 请求参数

```json
{
    "count": 1,
    "mediaProperties" : [
        {
            "fileType": "image",
            "mimeType": "image/svg+xml",
            "fileSize": 1024,
            "fileName": "svgtest202512222213"
        }
    ]
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|body|body|object| 否 |none|
|» count|body|integer| 是 |none|
|» mediaProperties|body|[object]| 是 |none|
|»» fileType|body|string| 否 |none|
|»» mimeType|body|string| 否 |none|
|»» fileSize|body|integer| 否 |none|
|»» fileName|body|string| 否 |none|

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

# agent

## GET session

GET /api/ai/sessions

> Body 请求参数

```
string

```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|limit|query|string| 是 |none|
|Content-Type|header|string| 是 |none|
|satoken|header|string| 是 |none|
|Cookie|header|string| 是 |none|
|body|body|string| 是 |none|

> 返回示例

> 200 Response

```json
{
  "code": 0,
  "message": "string",
  "data": {
    "ok": true,
    "data": [
      {
        "session_id": "string",
        "start_time": "string",
        "last_activity": "string",
        "turn_count": 0,
        "title": "string"
      }
    ]
  }
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» code|integer|true|none||none|
|» message|string|true|none||none|
|» data|object|true|none||none|
|»» ok|boolean|true|none||none|
|»» data|[object]|true|none||none|
|»»» session_id|string|true|none||none|
|»»» start_time|string|true|none||none|
|»»» last_activity|string|true|none||none|
|»»» turn_count|integer|true|none||none|
|»»» title|string|true|none||none|

## GET history

GET /api/ai/history

> Body 请求参数

```
string

```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|sessionId|query|string| 是 |none|
|Content-Type|header|string| 是 |none|
|satoken|header|string| 是 |none|
|Cookie|header|string| 是 |none|
|body|body|string| 是 |none|

> 返回示例

```json
{"code":200,"message":"ok","data":{"ok":true,"data":[{"id":"f396fba2152d44fcb833d89410d24c02","user_id":48,"user_query":"你好,你是谁","rewritten_queries":["你好 你是谁"],"retrieved_contexts":["# 作者简介","# 作者简介","# 作者简介","# 技术服务专家","# 作者简介"],"generated_answer":"你好，我是家禽养殖问答助手，可以为你提供家禽养殖相关的问题解答。","latency_ms":1308.1652000000759,"timestamp":"2026-05-05 21:40:16","user_rating":0,"expert_corrected_answer":null,"expert_corrected_queries":null,"status":"normal","llm_judge_score":5,"llm_judge_reason":"回答准确且完全基于上下文信息，明确表明身份为家禽养殖问答助手，没有添加任何幻觉内容，直接回应了用户问题。","qa_metadata":{"mode":"multi_branch_retrieval","sources":["白藜芦醇对乌骨鸡热应激损伤的修复机制研究_刘丽莉.md","甲状腺激素对乌鸡生长和蛋白质沉积的影响_曹满湖.md","雪峰乌骨鸡蛋氨酸需要量的研究_姚元枝.md","乌鸡黑色素的提取和测定及黑...过度沉积相关基因的研究进展_蒋明.md","甲状腺激素对乌鸡生长和蛋白质能量沉积的影响_高凤仙.md"],"session_id":"80fc06acdfbc4db6b85e025da9334f59","retrieval_debug":[{"k":12,"query":"你好,你是谁","branch":"primary_dense","weight":1.0,"raw_hits":12,"max_score":0.9527958631515504,"filter_used":false,"filter_candidates":0},{"k":10,"query":"你好 你是谁","branch":"normalized_dense","weight":0.72,"raw_hits":10,"max_score":0.9681469798088074,"filter_used":false,"filter_candidates":0}],"retrieval_max_score":0.9681469798088074,"session_memory_enabled":true},"expert_diagnosis":null,"expert_medication_advice":null,"review_reason":null,"kb_sync_status":null,"kb_sync_error":null,"kb_synced_at":null,"created_at":"2026-05-05 21:40:16","updated_at":"2026-05-05 21:40:18"},{"id":"ca34d03c2090471dbc8762849fbc907d","user_id":48,"user_query":"不错,牛逼","rewritten_queries":["不错 牛逼"],"retrieved_contexts":["# 经验交流","# ·经验交流·","# 作者简介","# 作者简介","# 1 实 验"],"generated_answer":"谢谢夸奖，我会尽力提供帮助。有什么家禽养殖方面的问题需要咨询吗？","latency_ms":1164.6054000011645,"timestamp":"2026-05-05 21:42:01","user_rating":0,"expert_corrected_answer":null,"expert_corrected_queries":null,"status":"normal","llm_judge_score":5,"llm_judge_reason":"回答完全基于上下文，没有编造内容，且直接回应了用户的夸奖，引导用户提出具体问题，相关性高且准确。","qa_metadata":{"mode":"multi_branch_retrieval","sources":["生态放养鸡新模式—枸杞园放养乌骨鸡_李瑞琴.md","添加花粉对黑羽乌骨鸡生长发育的影响_王建.md","甲状腺激素对乌鸡生长和蛋白质沉积的影响_曹满湖.md","雪峰乌骨鸡蛋氨酸需要量的研究_姚元枝.md","乌鸡白凤丸及其制剂中微量元素的研究_张奇凤.md"],"session_id":"80fc06acdfbc4db6b85e025da9334f59","retrieval_debug":[{"k":12,"query":"不错,牛逼","branch":"primary_dense","weight":1.0,"raw_hits":12,"max_score":0.9613349437713624,"filter_used":false,"filter_candidates":0},{"k":10,"query":"不错 牛逼","branch":"normalized_dense","weight":0.72,"raw_hits":10,"max_score":0.9281418919563292,"filter_used":false,"filter_candidates":0}],"retrieval_max_score":0.9613349437713624,"session_memory_enabled":true},"expert_diagnosis":null,"expert_medication_advice":null,"review_reason":null,"kb_sync_status":null,"kb_sync_error":null,"kb_synced_at":null,"created_at":"2026-05-05 21:42:01","updated_at":"2026-05-05 21:42:01"},{"id":"3ae61acdc1474d2689b7f86f29b42ac5","user_id":48,"user_query":"乌骨鸡有什么特征","rewritten_queries":["么特征 乌骨鸡 什么特 有什么 骨鸡有 鸡有什"],"retrieved_contexts":["# 乌骨鸡的外貌特征与生活习性","# 2乌蒙乌骨鸡的特征特性","# 1 乌骨鸡概况","# 一、 外貌特征  \n纯种乌骨鸡头小、 颈短、 遍体羽 毛 白色， 呈丝绒状 身体轻小，性情温驯， 行动迟缓 ， 外貌奇特美观。  \n概括起来有九大特征， 即续头、 丝冠、 绿耳、 胡须、 丝毛、 五爪、 毛腿、 乌骨、 乌皮。 无论公母鸡 ， 其眼睛、 咏、趾、 为黑色， 内脏与脂肪亦带黑色。 除主翼羽 、 副翼羽和部分公鸡的尾羽有分裂的羽 片外， 其余全身为绒羽 ， 每一羽 根可长 出 多支绒羽 ，护都的 羽 毛长至第一趾。 成年公鸡平均体重为 · ， 母鸡为。 成年乌骨鸡体尺测量见表  \n表 泰和乌骨鸡成鸡体尺表 单位\n![](images/6476b8f2d47ca063d26cefa32525c55f75514c14139d8a6ba141b95cf0fc4711.jpg)","# 1.2乌骨鸡的概况"],"generated_answer":"乌骨鸡的特征包括：头小、颈短、全身羽毛白色，呈丝绒状，身体轻小，性情温驯，行动迟缓，外貌奇特美观。其九大特征为：续头、丝冠、绿耳、胡须、丝毛、五爪、毛腿、乌骨、乌皮。无论公母鸡，其眼睛、喙、趾、皮肤均为黑色，内脏与脂肪亦带黑色。除主翼羽、副翼羽和部分公鸡的尾羽有分裂的羽片外，其余全身为绒羽，每一羽根可长出多支绒羽，羽毛长至第一趾。","latency_ms":2451.956599998084,"timestamp":"2026-05-05 21:45:11","user_rating":0,"expert_corrected_answer":null,"expert_corrected_queries":null,"status":"normal","llm_judge_score":5,"llm_judge_reason":"回答完全基于提供的上下文，准确描述了乌骨鸡的外貌特征和九大特征，没有添加任何幻觉内容，信息完整且直接回答了用户问题。","qa_metadata":{"mode":"multi_branch_retrieval","sources":["乌骨鸡的饲养_张宏伟.md","乌蒙乌骨鸡资源调查报告_廖正录.md","不同饲养方式对乌骨鸡生产性...品营养及药物残留的影响研究_左丽娟.md","泰和乌骨鸡生产性能观察_王纪亭.md","天府乌骨鸡第11世代的种用...同生产方式下的商品性能对比_彭向锐.md"],"session_id":"80fc06acdfbc4db6b85e025da9334f59","retrieval_debug":[{"k":12,"query":"乌骨鸡有什么特征","branch":"primary_dense","weight":1.0,"raw_hits":12,"max_score":0.3851154744625091,"filter_used":false,"filter_candidates":0},{"k":8,"query":"么特征 乌骨鸡 什么特 有什么 骨鸡有 鸡有什","branch":"keyword_dense","weight":0.42,"raw_hits":8,"max_score":0.3607916533946991,"filter_used":false,"filter_candidates":0}],"retrieval_max_score":0.3851154744625091,"session_memory_enabled":true},"expert_diagnosis":null,"expert_medication_advice":null,"review_reason":null,"kb_sync_status":null,"kb_sync_error":null,"kb_synced_at":null,"created_at":"2026-05-05 21:45:11","updated_at":"2026-05-05 21:45:12"},{"id":"6938fd0bbb8d4aa5b046a22f38f72977","user_id":48,"user_query":"回答不错","rewritten_queries":[],"retrieved_contexts":["# 解决方法","# 2.4讨论","# 2.4 讨论","# 2.4讨论","# 2.4 讨论"],"generated_answer":"谢谢夸奖，我会尽力提供帮助。有什么家禽养殖方面的问题需要咨询吗？","latency_ms":868.6549999983981,"timestamp":"2026-05-05 21:45:19","user_rating":0,"expert_corrected_answer":null,"expert_corrected_queries":null,"status":"normal","llm_judge_score":5,"llm_judge_reason":"回答完全基于上下文，简洁且直接回应了用户的夸奖，没有编造内容，相关性极高。","qa_metadata":{"mode":"multi_branch_retrieval","sources":["舞者生理期呵护宝典_Angelina.md","IL-21和肥大细胞在乌鸡...官发育过程中的分布及其变化_刘志伟.md","阿魏酸对脂多糖刺激天府乌骨鸡肠道及肝脏的影响_唐子婷.md","藏鸡胚胎低氧适应的转录组差异表达分析_王翔宇.md","鸡HMGCR基因多态性及其与经济性状相关性研究_魏杨.md"],"session_id":"80fc06acdfbc4db6b85e025da9334f59","retrieval_debug":[{"k":12,"query":"回答不错","branch":"primary_dense","weight":1.0,"raw_hits":12,"max_score":0.839218020439148,"filter_used":false,"filter_candidates":0}],"retrieval_max_score":0.839218020439148,"session_memory_enabled":true},"expert_diagnosis":null,"expert_medication_advice":null,"review_reason":null,"kb_sync_status":null,"kb_sync_error":null,"kb_synced_at":null,"created_at":"2026-05-05 21:45:19","updated_at":"2026-05-05 21:45:20"},{"id":"716c386445dd4c7dae7643b5eed600e7","user_id":48,"user_query":"乌骨鸡适合什么环境生长","rewritten_queries":["么环境 乌骨鸡 什么环 合什么 境生长 环境生"],"retrieved_contexts":["# 2 生态型乌骨鸡养殖的有利条件","# 一、鸡舍要求  \n乌骨鸡喜燥怕潮，场地应选择高而通风，阳光充足，冬暖夏凉不积水的小山丘，方向以坐北朝南为好。鸡舍内要干燥，要设地下火道或装置不散发炭气的暖气管等保温设备，在春寒湿重的季节里应在鸡舍地面铺一层木屑或谷壳，其他季节可垫沙粒，经常保持舍内清洁卫生。","# 5.2.2温度  \n育成鸡舍适宜温度为16～25℃,有利乌骨鸡生长。夏季炎热时,采取降低饲养密度与人工降温措施降低鸡舍温度。冬季寒冷时，采取适当添加饲料与鸡舍保温措施提升鸡舍温度。","# 饲养乌骨鸡","# 1 林地的选择  \n（1）为保证饲养的乌骨鸡的品质，需要提供良好的养殖地，林地的选择标准：地势平坦的高地或缓坡，排水良好、采光充足、通风良好的草坡、林地以及果园等。树林的遮荫度要在70%以上，保证鸡群在阳光强烈时能有树荫乘凉，预防鸡群中暑。  \n（2）隔离条件好，所选地距离干线公路、其他畜禽场、村镇以及居民点至少1 km以上。  \n（3）远离污染源，鸡场周围3 km内无大型化工厂、垃圾场以及矿厂等。  \n（4）禁止建场区域为生活饮用水源、食品厂上游，以免造成污染；禁止建设在风景名胜区、自然保护区以及国家或地方法律与法规不允许的区域内。"],"generated_answer":"乌骨鸡适合生长的环境包括：  \n1. 高而通风、阳光充足、冬暖夏凉不积水的小山丘，方向以坐北朝南为好。  \n2. 林地、草坡、果园等，要求地势平坦的高地或缓坡，排水良好、采光充足、通风良好，树林遮荫度在70%以上。  \n3. 隔离条件好，距离干线公路、其他畜禽场、村镇以及居民点至少1 km以上。  \n4. 远离污染源，周围3 km内无大型化工厂、垃圾场及矿厂等。  \n5. 舍内温度适宜为16～25℃，需注意夏季降温、冬季保温。","latency_ms":2293.674699998519,"timestamp":"2026-05-05 21:54:19","user_rating":0,"expert_corrected_answer":null,"expert_corrected_queries":null,"status":"normal","llm_judge_score":5,"llm_judge_reason":"回答完全基于提供的上下文，准确描述了乌骨鸡适合的生长环境，包括场地选择、温度要求和隔离条件，没有添加任何幻觉内容。","qa_metadata":{"mode":"multi_branch_retrieval","sources":["生态型乌骨鸡养殖技术_韩慧英.md","乌骨鸡饲养_卢祖生.md","乌骨鸡生态健康养殖技术_张朝廷.md","致富门路.md","赤水乌骨鸡林下生态养殖技术_赵其钞.md"],"session_id":"80fc06acdfbc4db6b85e025da9334f59","retrieval_debug":[{"k":12,"query":"乌骨鸡适合什么环境生长","branch":"primary_dense","weight":1.0,"raw_hits":12,"max_score":0.4365562796592712,"filter_used":false,"filter_candidates":0},{"k":8,"query":"么环境 乌骨鸡 什么环 合什么 境生长 环境生","branch":"keyword_dense","weight":0.42,"raw_hits":8,"max_score":0.4375231862068176,"filter_used":false,"filter_candidates":0}],"retrieval_max_score":0.4375231862068176,"session_memory_enabled":true},"expert_diagnosis":null,"expert_medication_advice":null,"review_reason":null,"kb_sync_status":null,"kb_sync_error":null,"kb_synced_at":null,"created_at":"2026-05-05 21:54:19","updated_at":"2026-05-05 21:54:20"},{"id":"9600823a564b4b899a4a94080121d35f","user_id":48,"user_query":"乌骨鸡什么味道的","rewritten_queries":["么味道 乌骨鸡 什么味 味道的 骨鸡什 鸡什么"],"retrieved_contexts":["# 1 乌骨鸡概况","# 四川山地乌骨鸡","# 盐津乌骨鸡简介","# 一、白色丝毛乌骨鸡","乌骨鸡为雉科动物，为家鸡的一种，一般羽毛为白色，其皮、 肉、骨、 嘴皆为乌色，也有黑毛乌骨的，斑毛乌骨的和肉白乌骨的。 乌骨鸡全身都是宝，其肉、蛋具有较好的营养和药用价值，是上等的疗病滋补佳品。"],"generated_answer":"根据现有信息，没有直接描述乌骨鸡的味道。但可以知道的是，乌骨鸡的肉和蛋具有较好的营养和药用价值，是上等的疗病滋补佳品。具体味道可能因饲养环境、饲料等因素而有所不同。","latency_ms":1542.5169999980426,"timestamp":"2026-05-05 22:00:57","user_rating":0,"expert_corrected_answer":null,"expert_corrected_queries":null,"status":"normal","llm_judge_score":4,"llm_judge_reason":"回答准确指出没有直接描述乌骨鸡的味道，同时引用了上下文中的营养和药用价值信息，相关性高且无幻觉。但未进一步推测或说明可能的风味特点，略显简略。","qa_metadata":{"mode":"multi_branch_retrieval","sources":["不同饲养方式对乌骨鸡生产性...品营养及药物残留的影响研究_左丽娟.md","四川山地乌骨鸡_申洪兵.md","盐津乌骨鸡简介_杨家义.md","介绍两种乌鸡优良品种.md","乌骨鸡的药用价值及食疗方法_金江.md"],"session_id":"80fc06acdfbc4db6b85e025da9334f59","retrieval_debug":[{"k":12,"query":"乌骨鸡什么味道的","branch":"primary_dense","weight":1.0,"raw_hits":12,"max_score":0.5009706616401672,"filter_used":false,"filter_candidates":0},{"k":8,"query":"么味道 乌骨鸡 什么味 味道的 骨鸡什 鸡什么","branch":"keyword_dense","weight":0.42,"raw_hits":8,"max_score":0.54908686876297,"filter_used":false,"filter_candidates":0}],"retrieval_max_score":0.54908686876297,"session_memory_enabled":true},"expert_diagnosis":null,"expert_medication_advice":null,"review_reason":null,"kb_sync_status":null,"kb_sync_error":null,"kb_synced_at":null,"created_at":"2026-05-05 22:00:57","updated_at":"2026-05-05 22:00:58"},{"id":"11019319d8d44efc9be1315740d37cfb","user_id":48,"user_query":"hello","rewritten_queries":[],"retrieved_contexts":["# 报 道","!","#","#","#"],"generated_answer":"你好！有什么家禽养殖方面的问题需要咨询吗？","latency_ms":965.9294000011869,"timestamp":"2026-05-05 22:01:59","user_rating":0,"expert_corrected_answer":null,"expert_corrected_queries":null,"status":"normal","llm_judge_score":5,"llm_judge_reason":"回答完全基于上下文，简洁且直接回应了用户问候，没有编造内容，相关性高。","qa_metadata":{"mode":"multi_branch_retrieval","sources":["胸腺提取液喂鸡的试验研究_王兴金.md","中华乌鸡的药用价值_李瑞生.md","乌蒙乌骨鸡Myf5基因多态性与生长体重的关联分析_王文涛.md","BCO2在黄色和白色皮肤鸡种中突变位点筛选_甘师仪.md","江山的旅游与美食_毛平.md"],"session_id":"80fc06acdfbc4db6b85e025da9334f59","retrieval_debug":[{"k":12,"query":"hello","branch":"primary_dense","weight":1.0,"raw_hits":12,"max_score":0.8139363527297974,"filter_used":false,"filter_candidates":0}],"retrieval_max_score":0.8139363527297974,"session_memory_enabled":true},"expert_diagnosis":null,"expert_medication_advice":null,"review_reason":null,"kb_sync_status":null,"kb_sync_error":null,"kb_synced_at":null,"created_at":"2026-05-05 22:01:59","updated_at":"2026-05-05 22:02:00"},{"id":"6d6b6a4f50fe4f158db194280cb06b07","user_id":48,"user_query":"你好啊","rewritten_queries":[],"retrieved_contexts":["# 报 道","# 招聘岗位","。","。","# 清楚。"],"generated_answer":"你好！有什么家禽养殖方面的问题需要咨询吗？","latency_ms":904.6130000024277,"timestamp":"2026-05-05 22:08:46","user_rating":0,"expert_corrected_answer":null,"expert_corrected_queries":null,"status":"normal","llm_judge_score":5,"llm_judge_reason":"回答简洁且完全基于上下文，没有编造内容，直接回应了用户问候，并引导其提出具体问题，符合要求。","qa_metadata":{"mode":"multi_branch_retrieval","sources":["胸腺提取液喂鸡的试验研究_王兴金.md","乌鸡黑色素的提取和测定及黑...过度沉积相关基因的研究进展_蒋明.md","淅乌骨鸡群体遗传多样性及绿壳蛋性状相关基因SNP分析_王帅豪.md","西畴乌骨鸡乌质性状评价标准建立及鸡冠乌度转录组学研究_康佳佳.md","鸡FATP4基因cDNA的...生物信息学分析及其组织表达_杨岭.md"],"session_id":"80fc06acdfbc4db6b85e025da9334f59","retrieval_debug":[{"k":12,"query":"你好啊","branch":"primary_dense","weight":1.0,"raw_hits":12,"max_score":0.8537145256996155,"filter_used":false,"filter_candidates":0}],"retrieval_max_score":0.8537145256996155,"session_memory_enabled":true},"expert_diagnosis":null,"expert_medication_advice":null,"review_reason":null,"kb_sync_status":null,"kb_sync_error":null,"kb_synced_at":null,"created_at":"2026-05-05 22:08:46","updated_at":"2026-05-05 22:08:47"},{"id":"42611df917e5458594a36319515a5654","user_id":48,"user_query":"你好啊","rewritten_queries":[],"retrieved_contexts":["# 报 道","# 招聘岗位","。","。","# 清楚。"],"generated_answer":"你好！有什么家禽养殖方面的问题需要咨询吗？","latency_ms":1003.6108000022068,"timestamp":"2026-05-05 22:19:12","user_rating":0,"expert_corrected_answer":null,"expert_corrected_queries":null,"status":"normal","llm_judge_score":5,"llm_judge_reason":"回答简洁且完全基于上下文，没有编造内容，直接回应了用户问候，并引导其提出具体问题，相关性高。","qa_metadata":{"mode":"multi_branch_retrieval","sources":["胸腺提取液喂鸡的试验研究_王兴金.md","乌鸡黑色素的提取和测定及黑...过度沉积相关基因的研究进展_蒋明.md","淅乌骨鸡群体遗传多样性及绿壳蛋性状相关基因SNP分析_王帅豪.md","西畴乌骨鸡乌质性状评价标准建立及鸡冠乌度转录组学研究_康佳佳.md","鸡FATP4基因cDNA的...生物信息学分析及其组织表达_杨岭.md"],"session_id":"80fc06acdfbc4db6b85e025da9334f59","retrieval_debug":[{"k":12,"query":"你好啊","branch":"primary_dense","weight":1.0,"raw_hits":12,"max_score":0.8537145256996155,"filter_used":false,"filter_candidates":0}],"retrieval_max_score":0.8537145256996155,"session_memory_enabled":true},"expert_diagnosis":null,"expert_medication_advice":null,"review_reason":null,"kb_sync_status":null,"kb_sync_error":null,"kb_synced_at":null,"created_at":"2026-05-05 22:19:12","updated_at":"2026-05-05 22:19:14"},{"id":"11ae80661ecc4b2c9c35f6985b72897f","user_id":48,"user_query":"hello","rewritten_queries":[],"retrieved_contexts":["# 报 道","!","#","#","#"],"generated_answer":"你好！有什么家禽养殖方面的问题需要咨询吗？","latency_ms":1068.0511999998998,"timestamp":"2026-05-05 22:19:42","user_rating":0,"expert_corrected_answer":null,"expert_corrected_queries":null,"status":"normal","llm_judge_score":5,"llm_judge_reason":"回答完全基于上下文，简洁且直接回应了用户问候，并引导用户提出具体问题，符合家禽养殖领域的专业沟通方式。","qa_metadata":{"mode":"multi_branch_retrieval","sources":["胸腺提取液喂鸡的试验研究_王兴金.md","中华乌鸡的药用价值_李瑞生.md","乌蒙乌骨鸡Myf5基因多态性与生长体重的关联分析_王文涛.md","BCO2在黄色和白色皮肤鸡种中突变位点筛选_甘师仪.md","江山的旅游与美食_毛平.md"],"session_id":"80fc06acdfbc4db6b85e025da9334f59","retrieval_debug":[{"k":12,"query":"hello","branch":"primary_dense","weight":1.0,"raw_hits":12,"max_score":0.8139363527297974,"filter_used":false,"filter_candidates":0}],"retrieval_max_score":0.8139363527297974,"session_memory_enabled":true},"expert_diagnosis":null,"expert_medication_advice":null,"review_reason":null,"kb_sync_status":null,"kb_sync_error":null,"kb_synced_at":null,"created_at":"2026-05-05 22:19:42","updated_at":"2026-05-05 22:19:43"},{"id":"205ee43fae6a475488e072a8986d45b3","user_id":48,"user_query":"hellooooo","rewritten_queries":[],"retrieved_contexts":["#","#","#","#","#"],"generated_answer":"你好！有什么家禽养殖方面的问题需要咨询吗？","latency_ms":2847.130099999049,"timestamp":"2026-05-06 10:45:36","user_rating":0,"expert_corrected_answer":null,"expert_corrected_queries":null,"status":"normal","llm_judge_score":5,"llm_judge_reason":"回答完全基于上下文，简洁且准确地回应了用户的问题，没有出现幻觉或偏离主题的情况。","qa_metadata":{"mode":"multi_branch_retrieval","sources":["BCO2在黄色和白色皮肤鸡种中突变位点筛选_甘师仪.md","不同品种乌骨鸡主要性状指标及其杂交效果比较研究_王建.md","中外鸡种MHC_B-L基因...变异及与免疫性状关系的研究_刘立波.md","寻找乌骨鸡_盛永明.md","枸杞园放养乌骨鸡血液指标、肉食用品质及风味物质研究_袁君.md"],"session_id":"80fc06acdfbc4db6b85e025da9334f59","retrieval_debug":[{"k":12,"query":"hellooooo","branch":"primary_dense","weight":1.0,"raw_hits":12,"max_score":0.7802712917327881,"filter_used":false,"filter_candidates":0}],"retrieval_max_score":0.7802712917327881,"session_memory_enabled":true},"expert_diagnosis":null,"expert_medication_advice":null,"review_reason":null,"kb_sync_status":null,"kb_sync_error":null,"kb_synced_at":null,"created_at":"2026-05-06 10:45:36","updated_at":"2026-05-06 10:45:41"},{"id":"19840c596df644ac9ae06088a1605ffa","user_id":48,"user_query":"hhhhh","rewritten_queries":[],"retrieved_contexts":["# !! \"","# !","# # !\"#\\$%","# # !\"#\\$%","#"],"generated_answer":"你好！有什么家禽养殖方面的问题需要咨询吗？","latency_ms":1976.5641000012693,"timestamp":"2026-05-06 11:23:47","user_rating":0,"expert_corrected_answer":null,"expert_corrected_queries":null,"status":"normal","llm_judge_score":1,"llm_judge_reason":"回答完全偏离用户问题，用户输入的是无意义字符串 'hhhhh'，而系统回复了无关的问候语，未提供任何有用信息。","qa_metadata":{"mode":"multi_branch_retrieval","sources":["枸杞园放养乌骨鸡肉食用品质与血清生化指标相关性分析_袁君.md","浅析乌鸡的药用及保健价值_王晓通.md","孵化期间乌骨鸡种蛋水分和钙变化及胚胎生长的研究_房兴堂.md","乌蒙乌骨鸡Myf5基因多态性与生长体重的关联分析_王文涛.md"],"session_id":"80fc06acdfbc4db6b85e025da9334f59","retrieval_debug":[{"k":12,"query":"hhhhh","branch":"primary_dense","weight":1.0,"raw_hits":12,"max_score":0.7942354679107666,"filter_used":false,"filter_candidates":0}],"retrieval_max_score":0.7942354679107666,"session_memory_enabled":true},"expert_diagnosis":null,"expert_medication_advice":null,"review_reason":null,"kb_sync_status":null,"kb_sync_error":null,"kb_synced_at":null,"created_at":"2026-05-06 11:23:47","updated_at":"2026-05-06 11:23:48"},{"id":"26133a11eecb4f3eb4719bb75df00340","user_id":48,"user_query":"你很会养鸡吗","rewritten_queries":["会养鸡 你很会 养鸡吗 很会养 会养 你很"],"retrieved_contexts":["# 3掌握好饲养密度  \n密度过大，鸡的活动采食受限制，生长慢，发育不齐，空气流通差，易发生啄癖，死亡率高；密度过小则浪费人力物力。一般平面饲养每平方米1一2周龄50羽，3—5周龄30羽，6-8周龄20羽，9-13周龄10羽。此外还应注意鸡群数量不宜太大，以每群饲养200－300羽为宜。","# 营养需要  \n成 年 鸡 的 营 养 需 要 见 表 ， 在 按 表 配 制  \n日粮时也需添加维生素及微量元素添加剂。  \n度年劝的， 共\n![](images/cdd712a644c7ae5a06f55d689f49a6903a55aeaf595f6810f1d57ac375c5cfc4.jpg)","# 2 场地的选择  \n一般要选择丘陵 山地 果场 高山半山腰 山顶等 场地最好能与农作物有一段距离 以免鸡损坏农作物 如果离农作物较近 则要用围网把农作物围起来 饲养地点最好选择能用水管引得到山泉为优以减轻供水的劳动力。如果有机耕路到达则最好 可以方便饲料和鸡的运输 场地的树林 灌木、树叶、杂草不必清除可以供鸡采食还可以遮荫。可以适当饲养几条家狗 一可以驱除天敌 二可以防人偷鸡 鸡在多个场地进行轮流放养每个场地应有一定的距离以防疫病交叉感染 给场地一定时间的休养期 可以利用阳光 雨水等自然条件消除病菌 还可以有利于植被 杂草的生长和恢复","# 5.2搞好清洁卫生  \n鸡场内外，鸡舍内外必须经常清扫，食槽、饮水器、生产工具要定期洗刷、消毒。每天定时清粪，定期灭鼠、杀虫，消除疾病的传摇媒介，及时清除垃圾，消灭蚊蝇，及时消除死鸡，隔离病鸡。","# 三、科学饲养  \n饲养 舍饲 因地制宜建造一个冬暖以上 夏凉 以下 高燥 通风良好 坐北朝南的鸡舍。舍前设运动场每平方米不超过4只。（2）放牧：防疫条件好的可放牧饲养 笼养 同大批量养蛋鸡一样但鸡舍造价和设备投资相对高些。（4）利用塑料大棚饲养 夏天炎热 可在棚顶加盖草帘  \n2．饲料 根据用途不同选择相应的饲料。料量 周龄 每只日饲料消耗量 克 周龄 克周龄 克 周龄 克 周龄 克 周龄 克周龄50克成鸡60克左右。（1）选择蛋鸡全价料从雏鸡开始选择料号饲喂 肉用 选择肉鸡全价料日龄～30日龄雏鸡全价料；30日龄～60日龄中雏生长料；60日龄～120日龄育肥全价料。（3）药用：选择特制的中草药做为添加剂 于 日龄后添加 直到出栏。  \n3．繁育 （1）机械孵化：适用于大中型养鸡场。养户可购置小型孵化器或电孵化箱 火炕孵化 在火炕上铺可控温的电褥子其上面放热水袋后加盖棉被 温度保持在 传统孵化 可用就巢鸡孵化  \n（061001 河北沧州农校）"],"generated_answer":"我具备一定的家禽养殖知识，可以为你提供相关指导。如果你有具体问题，欢迎随时提问！","latency_ms":1866.9961000000512,"timestamp":"2026-05-06 16:02:44","user_rating":0,"expert_corrected_answer":null,"expert_corrected_queries":null,"status":"normal","llm_judge_score":3,"llm_judge_reason":"回答较为笼统，虽然没有明显幻觉，但未直接展示具体养殖知识，也未引用上下文中的具体内容，相关性一般。","qa_metadata":{"mode":"multi_branch_retrieval","sources":["商品乌骨鸡饲养管理技术要点_吴光程.md","乌骨鸡的饲养管理技术_康相涛.md","西畴山地乌骨鸡的饲养管理_陆灵勇.md","乌鸡的规模化养殖技术_汪莉.md","科学饲养乌骨鸡_孙庆伟.md"],"session_id":"80fc06acdfbc4db6b85e025da9334f59","retrieval_debug":[{"k":12,"query":"你很会养鸡吗","branch":"primary_dense","weight":1.0,"raw_hits":12,"max_score":0.6874305009841919,"filter_used":false,"filter_candidates":0},{"k":8,"query":"会养鸡 你很会 养鸡吗 很会养 会养 你很","branch":"keyword_dense","weight":0.42,"raw_hits":8,"max_score":0.7068487405776978,"filter_used":false,"filter_candidates":0}],"retrieval_max_score":0.7068487405776978,"session_memory_enabled":true},"expert_diagnosis":null,"expert_medication_advice":null,"review_reason":null,"kb_sync_status":null,"kb_sync_error":null,"kb_synced_at":null,"created_at":"2026-05-06 16:02:44","updated_at":"2026-05-06 16:02:46"},{"id":"623c4d331a3c4115bb552e24e560d7c7","user_id":48,"user_query":"乌骨鸡生病了怎么办","rewritten_queries":["乌骨鸡 了怎么 怎么办 生病了 病了怎 骨鸡生"],"retrieved_contexts":["防止应激。 乌骨鸡胆小怕惊， 对各种应激敏感性特强 ， 因此 除提供一 个安静的饲养环境外 工 作时还应注意做到轻、 慢 防止应激发生。  \n劫观察。 乌骨鸡对外界抵抗能力差， 易发生各种疾 病。 因此应勤观察 发现离群呆立 精神不好 不吃 食或粪便异常的鸡， 应及时隔离治疗 加强护理。  \n疾病防治。 为了保证乌骨鸡的健康生长 减少经济损失 必 须做好疾病防治 坚持无病早防 有病早治的原则。 鸡舍门口 设消毒池 ， 禁止 外人 参观 ， 做好育雏舍及 用 具 的清洁卫生与消毒。 根据 乌骨鸡发病规律， 进行合理 投 药。 育雏的前 周内， 应 在饲 料或饮水中添 加抗生素 如土霉素、 氯霉素、 痢特灵 等药物 ， 预防鸡白痢和大肠杆菌病 第 周起在饲料中添 加 抗 球 虫 药 ， 如 球 虫 灵 、 氯 苯 版 、 克球 粉 等 防 止 球虫病发 生 与此 同时 还 崖根 据本地疫 病流 行情 况制定科学的疫苗免疫程 序， 适时地进行 各种免疫注射 以防各种疫病的发生流行。","此 外 大 肠 杆 菌 病 、 禽 霍 乱 、 啄 癖 等 也 有 发 生 。 因此， 要 保证乌骨鸡 的健康生 长， 提高成活率， 减少 经济损失， 必 须做好各种疫病的综合防制， 坚持无病早 防、有 病 早 治 的 原 则 。 鸡 群 实 行 “ 全 进 全 出 ” 制 ， 鸡 舍 门 口设 消毒池， 禁止 外来人 员参观， 并做好鸡舍及 用具 的清洁卫 生与消毒， 防止 雏鸡早期 感染。 加 强饲养管理，提 供优质全 价饲 料， 减少各种应激， 搞好 鸡舍通 风 换气， 勤换垫 料， 减少舍 内氨气 等有害气体， 改善鸡 群 生活环境。 根据乌骨鸡的发病规律 进行合理 用药， 育雏的前 周 内， 应在饲料或饮水 中添 加抗 菌 药物， 如土母素、 抓霉素、 痢特灵 等， 预 防鸡 白痢 杆菌病和 大肠 杆菌病 第 周起 至 日龄 在饲 料 中添 加 抓苯肌 克球粉、 抗球 王等抗球虫药， 预 防鸡球虫病 在 呼 吸道病 的多发季节， 应常用链雄素、 红霉 素等药物 预 防。 应根据本地疫病流行情况， 制定科学 的免疫 程序， 适 时地进行各种免 疫接种， 以 防各种疫病 的发 生与流行。 豆","# 1. 卫生防疫工作  \n乌骨鸡抗病力较差 所以要特别强调卫生防疫工作 饲养前鸡舍内外应用 烧碱水彻底消毒 墙壁用 石灰水刷白 每半个月用 百毒杀消毒 次 垫草要勤换 食槽饮水器应每天洗刷干净 清扫出的粪便 垫草要远离鸡舍作无公害处理 周龄内雏鸡,每天应在饲料中加入适量痢特灵防止副伤寒和白痢病。2周龄后加适量痢特灵 停 天后再喂 次 周龄后要防治蛔虫病 另外 周龄时还要注射禽流感和鸡新城疫疫苗","# 二、疾病防治  \n乌骨鸡体质娇弱 抗病力较差 必须搞好疾病的防治工作 首先 要搞好饮水和饲料卫生 经常打扫鸡舍和运动场定期消毒。其次建立健全防疫制度及时进行鸡新城疫、禽霍乱等疫苗的防疫注射和药物预防特别要注意对马立克氏病和硬嗉病两个易发病的防治。","乌骨鸡的抗病力较其他鸡差，易受各种疾病的威胁，要养好乌骨鸡，疾病防治也很关键。目前，为害乌骨鸡的疾病有：新城疫、球虫病，鸡白痢，马立克氏病、鸡法氏囊病和鸡痘等，具体的预防办法和预防程序与普通产蛋鸡大致相同。"],"generated_answer":"如果乌骨鸡生病了，可以采取以下措施：  \n1. **及时隔离病鸡**，避免疾病传播。  \n2. **观察症状**，如精神不好、不吃食、粪便异常等，根据症状对症处理。  \n3. **保持环境清洁**，加强鸡舍消毒，勤换垫料，改善通风条件。  \n4. **合理用药**，根据疾病类型在饲料或饮水中添加抗生素或抗球虫药（如土霉素、痢特灵、球虫灵等）。  \n5. **做好疫苗免疫**，根据当地疫情进行新城疫、禽流感等疫苗接种。  \n\n建议根据具体病情咨询专业兽医进行针对性治疗。","latency_ms":6804.834099999425,"timestamp":"2026-05-06 22:19:59","user_rating":0,"expert_corrected_answer":null,"expert_corrected_queries":null,"status":"normal","llm_judge_score":5,"llm_judge_reason":"回答完全基于提供的上下文，准确列出了隔离病鸡、观察症状、环境清洁、合理用药和疫苗免疫等措施，没有编造内容，且直接解决了用户问题。","qa_metadata":{"mode":"multi_branch_retrieval","sources":["提高乌骨鸡育雏成活率的八项技术措施_吴光程.md","乌骨鸡五大疫病的防制_吴光程.md","乌骨鸡的科学养殖_洪森旗.md","乌骨鸡的开发利用_李建鑫.md","乌骨鸡及其饲养技术_王长生.md"],"session_id":"80fc06acdfbc4db6b85e025da9334f59","retrieval_debug":[{"k":12,"query":"乌骨鸡生病了怎么办","branch":"primary_dense","weight":1.0,"raw_hits":12,"max_score":0.368893563747406,"filter_used":false,"filter_candidates":0},{"k":8,"query":"乌骨鸡 了怎么 怎么办 生病了 病了怎 骨鸡生","branch":"keyword_dense","weight":0.42,"raw_hits":8,"max_score":0.3802187144756317,"filter_used":false,"filter_candidates":0}],"retrieval_max_score":0.3802187144756317,"session_memory_enabled":true},"expert_diagnosis":null,"expert_medication_advice":null,"review_reason":null,"kb_sync_status":null,"kb_sync_error":null,"kb_synced_at":null,"created_at":"2026-05-06 22:19:59","updated_at":"2026-05-06 22:20:01"},{"id":"9d178fe75b734388be25156ee03cb28d","user_id":48,"user_query":"乌骨鸡长什么样","rewritten_queries":["乌骨鸡 什么样 长什么 骨鸡长 鸡长什 么样"],"retrieved_contexts":["# 乌骨鸡的外貌特征与生活习性","# 一、 外貌特征  \n纯种乌骨鸡头小、 颈短、 遍体羽 毛 白色， 呈丝绒状 身体轻小，性情温驯， 行动迟缓 ， 外貌奇特美观。  \n概括起来有九大特征， 即续头、 丝冠、 绿耳、 胡须、 丝毛、 五爪、 毛腿、 乌骨、 乌皮。 无论公母鸡 ， 其眼睛、 咏、趾、 为黑色， 内脏与脂肪亦带黑色。 除主翼羽 、 副翼羽和部分公鸡的尾羽有分裂的羽 片外， 其余全身为绒羽 ， 每一羽 根可长 出 多支绒羽 ，护都的 羽 毛长至第一趾。 成年公鸡平均体重为 · ， 母鸡为。 成年乌骨鸡体尺测量见表  \n表 泰和乌骨鸡成鸡体尺表 单位\n![](images/6476b8f2d47ca063d26cefa32525c55f75514c14139d8a6ba141b95cf0fc4711.jpg)","# 1.1.1乌骨鸡的外貌特征  \n从观赏型角度来评价，传统标准的乌骨鸡具有丝毛、缨头、凤冠、绿耳、胡须、毛腿、五爪、乌皮、乌肉、乌骨的\"十全\"特征；从商品生产角度出发，主要为肉用，按体型大小分为大、中、小三型；依据羽毛颜色划分，有白色、黑色、杂色三型；按羽毛类型分，有丝羽、片羽、卷羽三类[5]。","# 1乌骨鸡的外貌特征和性能  \n纯种乌骨鸡头小，颈短，遍体 羽毛白色，呈丝绒状。身体轻小，性情温驷，行动迟缓，外貌奇特美观。概括起来有十大特征：①缨头。头顶有一小摄较长的丝毛，形如白绒球，形成毛冠。②丝冠。又称玫瑰冠，呈紫黑色，又如桑椹状，母鸡冠小，公鸡冠大。③绿耳。耳叶蓝中透绿，呈孔雀藍色，在性成熟初期更鲜艳夺目。④胡须。从耳根到两颈、下颌有较长的细毛，形同挂须，母鸡发达。③丝毛。全身羽毛洁白，除两翅主翼和尾羽的箭形羽毛外，全呈绒状细毛。⑥五爪。即脚上有五趾。③毛腿。从部到脚趾基部密生白毛。⑧乌骨。骨质骨膜均暗乌。③乌肉。肌肉、内脏及脂肪均墨乌黑色。①乌皮。开身皮肤及眼、喙、爪均为黑色，由此而得名乌骨鸡。但生产实践中区别真假纯种乌骨鸟是看乌骨鸡的鸡舌，鸡舌乌黑（浅乌）者，才是品质优良的岛骨鸡，就是“观鸡舌黑者，则肉骨具乌”。  \n乌骨鸡的初生体重平均为26.9g，成年公鸡体重1.2～1.6kg，平均160天开啼；母鸡1.0～12.5kg，160～180天开产，年平均产蛋80～150枚，蛋重40～46g，蛋壳棕褐色，母鸡就巢性强，每产约30枚蛋就停产“抱窝”一次，每次持续时间30天以上，每年“抱窝”6～7次。","# 二 、 体型外貌  \n山地 乌骨鸡 以冠、 嚎、 髯、 舌 、 皮、 肉、 骨及 内脏含油 乌黑为主要特征 ， 尤乌皮、 乌 肉、 乌骨“ 三乌”羽色以 紫兰色黑毛居多 ， 斑毛及 白毛次之 羽型 以常羽为主 ， 反羽 狗毛 和丝羽 次之。  \n公鸡体格高大 ， 母鸡体格适 中 相对“ 丝羽 乌骨鸡” 为大 型 品种 ， 躯体发育匀称， 体型 轮廊近似“ 砖型 ” 、 “ 船 型 ” 、 “ 方 型 ” 。 山 地 乌 骨 鸡 成 年 体 重 公 鸡 平 均一 公斤 有的可达 公斤 ， 母鸡 一公斤 有的可达 公斤 。"],"generated_answer":"乌骨鸡的外貌特征如下：\n\n- 头小、颈短，全身羽毛白色，呈丝绒状。\n- 具有九大特征：缨头（头顶有白绒球）、丝冠（紫黑色玫瑰冠）、绿耳（蓝中透绿的耳叶）、胡须（下颌有细长毛）、丝毛（全身绒状细毛）、五爪（脚上有五趾）、毛腿（从腿部到脚趾基部密生白毛）、乌骨（骨质和骨膜乌黑）、乌皮（皮肤、眼、喙、爪均为黑色）。\n- 除主翼羽、副翼羽和部分公鸡的尾羽外，全身为绒羽。\n- 成年公鸡体重约1.2～1.6公斤，母鸡约1.0～1.25公斤。","latency_ms":36508.20109999768,"timestamp":"2026-05-06 23:10:36","user_rating":0,"expert_corrected_answer":null,"expert_corrected_queries":null,"status":"normal","llm_judge_score":5,"llm_judge_reason":"回答完全基于检索到的上下文，准确描述了乌骨鸡的外貌特征，包括九大特征、羽毛类型、体重等信息，没有编造内容，且直接回答了用户问题。","qa_metadata":{"mode":"multi_branch_retrieval","sources":["乌骨鸡的饲养_张宏伟.md","泰和乌骨鸡生产性能观察_王纪亭.md","不同品种乌骨鸡主要性状指标及其杂交效果比较研究_王建.md","乌骨鸡及其饲养技术_王长生.md","山地乌骨鸡高产饲养技术研究...地乌骨鸡品种综述及开发现状_王成.md"],"session_id":"80fc06acdfbc4db6b85e025da9334f59","retrieval_debug":[{"k":12,"query":"乌骨鸡长什么样","branch":"primary_dense","weight":1.0,"raw_hits":12,"max_score":0.3839451670646667,"filter_used":false,"filter_candidates":0},{"k":8,"query":"乌骨鸡 什么样 长什么 骨鸡长 鸡长什 么样","branch":"keyword_dense","weight":0.42,"raw_hits":8,"max_score":0.3658551573753357,"filter_used":false,"filter_candidates":0}],"retrieval_max_score":0.3839451670646667,"session_memory_enabled":true},"expert_diagnosis":null,"expert_medication_advice":null,"review_reason":null,"kb_sync_status":null,"kb_sync_error":null,"kb_synced_at":null,"created_at":"2026-05-06 23:10:36","updated_at":"2026-05-06 23:11:01"}]}}
```

```json
{
    "code": 200,
    "message": "ok",
    "data": {
        "ok": true,
        "data": [
            {
                "id": "f396fba2152d44fcb833d89410d24c02",
                "user_id": 48,
                "user_query": "你好,你是谁",
                "rewritten_queries": [
                    "你好 你是谁"
                ],
                "retrieved_contexts": [
                    "# 作者简介",
                    "# 作者简介",
                    "# 作者简介",
                    "# 技术服务专家",
                    "# 作者简介"
                ],
                "generated_answer": "你好，我是家禽养殖问答助手，可以为你提供家禽养殖相关的问题解答。",
                "latency_ms": 1308.1652000000759,
                "timestamp": "2026-05-05 21:40:16",
                "user_rating": 0,
                "expert_corrected_answer": null,
                "expert_corrected_queries": null,
                "status": "normal",
                "llm_judge_score": 5,
                "llm_judge_reason": "回答准确且完全基于上下文信息，明确表明身份为家禽养殖问答助手，没有添加任何幻觉内容，直接回应了用户问题。",
                "qa_metadata": {
                    "mode": "multi_branch_retrieval",
                    "sources": [
                        "白藜芦醇对乌骨鸡热应激损伤的修复机制研究_刘丽莉.md",
                        "甲状腺激素对乌鸡生长和蛋白质沉积的影响_曹满湖.md",
                        "雪峰乌骨鸡蛋氨酸需要量的研究_姚元枝.md",
                        "乌鸡黑色素的提取和测定及黑...过度沉积相关基因的研究进展_蒋明.md",
                        "甲状腺激素对乌鸡生长和蛋白质能量沉积的影响_高凤仙.md"
                    ],
                    "session_id": "80fc06acdfbc4db6b85e025da9334f59",
                    "retrieval_debug": [
                        {
                            "k": 12,
                            "query": "你好,你是谁",
                            "branch": "primary_dense",
                            "weight": 1.0,
                            "raw_hits": 12,
                            "max_score": 0.9527958631515504,
                            "filter_used": false,
                            "filter_candidates": 0
                        },
                        {
                            "k": 10,
                            "query": "你好 你是谁",
                            "branch": "normalized_dense",
                            "weight": 0.72,
                            "raw_hits": 10,
                            "max_score": 0.9681469798088074,
                            "filter_used": false,
                            "filter_candidates": 0
                        }
                    ],
                    "retrieval_max_score": 0.9681469798088074,
                    "session_memory_enabled": true
                },
                "expert_diagnosis": null,
                "expert_medication_advice": null,
                "review_reason": null,
                "kb_sync_status": null,
                "kb_sync_error": null,
                "kb_synced_at": null,
                "created_at": "2026-05-05 21:40:16",
                "updated_at": "2026-05-05 21:40:18"
            },
            {
                "id": "ca34d03c2090471dbc8762849fbc907d",
                "user_id": 48,
                "user_query": "不错,牛逼",
                "rewritten_queries": [
                    "不错 牛逼"
                ],
                "retrieved_contexts": [
                    "# 经验交流",
                    "# ·经验交流·",
                    "# 作者简介",
                    "# 作者简介",
                    "# 1 实 验"
                ],
                "generated_answer": "谢谢夸奖，我会尽力提供帮助。有什么家禽养殖方面的问题需要咨询吗？",
                "latency_ms": 1164.6054000011645,
                "timestamp": "2026-05-05 21:42:01",
                "user_rating": 0,
                "expert_corrected_answer": null,
                "expert_corrected_queries": null,
                "status": "normal",
                "llm_judge_score": 5,
                "llm_judge_reason": "回答完全基于上下文，没有编造内容，且直接回应了用户的夸奖，引导用户提出具体问题，相关性高且准确。",
                "qa_metadata": {
                    "mode": "multi_branch_retrieval",
                    "sources": [
                        "生态放养鸡新模式—枸杞园放养乌骨鸡_李瑞琴.md",
                        "添加花粉对黑羽乌骨鸡生长发育的影响_王建.md",
                        "甲状腺激素对乌鸡生长和蛋白质沉积的影响_曹满湖.md",
                        "雪峰乌骨鸡蛋氨酸需要量的研究_姚元枝.md",
                        "乌鸡白凤丸及其制剂中微量元素的研究_张奇凤.md"
                    ],
                    "session_id": "80fc06acdfbc4db6b85e025da9334f59",
                    "retrieval_debug": [
                        {
                            "k": 12,
                            "query": "不错,牛逼",
                            "branch": "primary_dense",
                            "weight": 1.0,
                            "raw_hits": 12,
                            "max_score": 0.9613349437713624,
                            "filter_used": false,
                            "filter_candidates": 0
                        },
                        {
                            "k": 10,
                            "query": "不错 牛逼",
                            "branch": "normalized_dense",
                            "weight": 0.72,
                            "raw_hits": 10,
                            "max_score": 0.9281418919563292,
                            "filter_used": false,
                            "filter_candidates": 0
                        }
                    ],
                    "retrieval_max_score": 0.9613349437713624,
                    "session_memory_enabled": true
                },
                "expert_diagnosis": null,
                "expert_medication_advice": null,
                "review_reason": null,
                "kb_sync_status": null,
                "kb_sync_error": null,
                "kb_synced_at": null,
                "created_at": "2026-05-05 21:42:01",
                "updated_at": "2026-05-05 21:42:01"
            },
            {
                "id": "3ae61acdc1474d2689b7f86f29b42ac5",
                "user_id": 48,
                "user_query": "乌骨鸡有什么特征",
                "rewritten_queries": [
                    "么特征 乌骨鸡 什么特 有什么 骨鸡有 鸡有什"
                ],
                "retrieved_contexts": [
                    "# 乌骨鸡的外貌特征与生活习性",
                    "# 2乌蒙乌骨鸡的特征特性",
                    "# 1 乌骨鸡概况",
                    "# 一、 外貌特征  \n纯种乌骨鸡头小、 颈短、 遍体羽 毛 白色， 呈丝绒状 身体轻小，性情温驯， 行动迟缓 ， 外貌奇特美观。  \n概括起来有九大特征， 即续头、 丝冠、 绿耳、 胡须、 丝毛、 五爪、 毛腿、 乌骨、 乌皮。 无论公母鸡 ， 其眼睛、 咏、趾、 为黑色， 内脏与脂肪亦带黑色。 除主翼羽 、 副翼羽和部分公鸡的尾羽有分裂的羽 片外， 其余全身为绒羽 ， 每一羽 根可长 出 多支绒羽 ，护都的 羽 毛长至第一趾。 成年公鸡平均体重为 · ， 母鸡为。 成年乌骨鸡体尺测量见表  \n表 泰和乌骨鸡成鸡体尺表 单位\n![](images/6476b8f2d47ca063d26cefa32525c55f75514c14139d8a6ba141b95cf0fc4711.jpg)",
                    "# 1.2乌骨鸡的概况"
                ],
                "generated_answer": "乌骨鸡的特征包括：头小、颈短、全身羽毛白色，呈丝绒状，身体轻小，性情温驯，行动迟缓，外貌奇特美观。其九大特征为：续头、丝冠、绿耳、胡须、丝毛、五爪、毛腿、乌骨、乌皮。无论公母鸡，其眼睛、喙、趾、皮肤均为黑色，内脏与脂肪亦带黑色。除主翼羽、副翼羽和部分公鸡的尾羽有分裂的羽片外，其余全身为绒羽，每一羽根可长出多支绒羽，羽毛长至第一趾。",
                "latency_ms": 2451.956599998084,
                "timestamp": "2026-05-05 21:45:11",
                "user_rating": 0,
                "expert_corrected_answer": null,
                "expert_corrected_queries": null,
                "status": "normal",
                "llm_judge_score": 5,
                "llm_judge_reason": "回答完全基于提供的上下文，准确描述了乌骨鸡的外貌特征和九大特征，没有添加任何幻觉内容，信息完整且直接回答了用户问题。",
                "qa_metadata": {
                    "mode": "multi_branch_retrieval",
                    "sources": [
                        "乌骨鸡的饲养_张宏伟.md",
                        "乌蒙乌骨鸡资源调查报告_廖正录.md",
                        "不同饲养方式对乌骨鸡生产性...品营养及药物残留的影响研究_左丽娟.md",
                        "泰和乌骨鸡生产性能观察_王纪亭.md",
                        "天府乌骨鸡第11世代的种用...同生产方式下的商品性能对比_彭向锐.md"
                    ],
                    "session_id": "80fc06acdfbc4db6b85e025da9334f59",
                    "retrieval_debug": [
                        {
                            "k": 12,
                            "query": "乌骨鸡有什么特征",
                            "branch": "primary_dense",
                            "weight": 1.0,
                            "raw_hits": 12,
                            "max_score": 0.3851154744625091,
                            "filter_used": false,
                            "filter_candidates": 0
                        },
                        {
                            "k": 8,
                            "query": "么特征 乌骨鸡 什么特 有什么 骨鸡有 鸡有什",
                            "branch": "keyword_dense",
                            "weight": 0.42,
                            "raw_hits": 8,
                            "max_score": 0.3607916533946991,
                            "filter_used": false,
                            "filter_candidates": 0
                        }
                    ],
                    "retrieval_max_score": 0.3851154744625091,
                    "session_memory_enabled": true
                },
                "expert_diagnosis": null,
                "expert_medication_advice": null,
                "review_reason": null,
                "kb_sync_status": null,
                "kb_sync_error": null,
                "kb_synced_at": null,
                "created_at": "2026-05-05 21:45:11",
                "updated_at": "2026-05-05 21:45:12"
            },
            {
                "id": "6938fd0bbb8d4aa5b046a22f38f72977",
                "user_id": 48,
                "user_query": "回答不错",
                "rewritten_queries": [],
                "retrieved_contexts": [
                    "# 解决方法",
                    "# 2.4讨论",
                    "# 2.4 讨论",
                    "# 2.4讨论",
                    "# 2.4 讨论"
                ],
                "generated_answer": "谢谢夸奖，我会尽力提供帮助。有什么家禽养殖方面的问题需要咨询吗？",
                "latency_ms": 868.6549999983981,
                "timestamp": "2026-05-05 21:45:19",
                "user_rating": 0,
                "expert_corrected_answer": null,
                "expert_corrected_queries": null,
                "status": "normal",
                "llm_judge_score": 5,
                "llm_judge_reason": "回答完全基于上下文，简洁且直接回应了用户的夸奖，没有编造内容，相关性极高。",
                "qa_metadata": {
                    "mode": "multi_branch_retrieval",
                    "sources": [
                        "舞者生理期呵护宝典_Angelina.md",
                        "IL-21和肥大细胞在乌鸡...官发育过程中的分布及其变化_刘志伟.md",
                        "阿魏酸对脂多糖刺激天府乌骨鸡肠道及肝脏的影响_唐子婷.md",
                        "藏鸡胚胎低氧适应的转录组差异表达分析_王翔宇.md",
                        "鸡HMGCR基因多态性及其与经济性状相关性研究_魏杨.md"
                    ],
                    "session_id": "80fc06acdfbc4db6b85e025da9334f59",
                    "retrieval_debug": [
                        {
                            "k": 12,
                            "query": "回答不错",
                            "branch": "primary_dense",
                            "weight": 1.0,
                            "raw_hits": 12,
                            "max_score": 0.839218020439148,
                            "filter_used": false,
                            "filter_candidates": 0
                        }
                    ],
                    "retrieval_max_score": 0.839218020439148,
                    "session_memory_enabled": true
                },
                "expert_diagnosis": null,
                "expert_medication_advice": null,
                "review_reason": null,
                "kb_sync_status": null,
                "kb_sync_error": null,
                "kb_synced_at": null,
                "created_at": "2026-05-05 21:45:19",
                "updated_at": "2026-05-05 21:45:20"
            },
            {
                "id": "716c386445dd4c7dae7643b5eed600e7",
                "user_id": 48,
                "user_query": "乌骨鸡适合什么环境生长",
                "rewritten_queries": [
                    "么环境 乌骨鸡 什么环 合什么 境生长 环境生"
                ],
                "retrieved_contexts": [
                    "# 2 生态型乌骨鸡养殖的有利条件",
                    "# 一、鸡舍要求  \n乌骨鸡喜燥怕潮，场地应选择高而通风，阳光充足，冬暖夏凉不积水的小山丘，方向以坐北朝南为好。鸡舍内要干燥，要设地下火道或装置不散发炭气的暖气管等保温设备，在春寒湿重的季节里应在鸡舍地面铺一层木屑或谷壳，其他季节可垫沙粒，经常保持舍内清洁卫生。",
                    "# 5.2.2温度  \n育成鸡舍适宜温度为16～25℃,有利乌骨鸡生长。夏季炎热时,采取降低饲养密度与人工降温措施降低鸡舍温度。冬季寒冷时，采取适当添加饲料与鸡舍保温措施提升鸡舍温度。",
                    "# 饲养乌骨鸡",
                    "# 1 林地的选择  \n（1）为保证饲养的乌骨鸡的品质，需要提供良好的养殖地，林地的选择标准：地势平坦的高地或缓坡，排水良好、采光充足、通风良好的草坡、林地以及果园等。树林的遮荫度要在70%以上，保证鸡群在阳光强烈时能有树荫乘凉，预防鸡群中暑。  \n（2）隔离条件好，所选地距离干线公路、其他畜禽场、村镇以及居民点至少1 km以上。  \n（3）远离污染源，鸡场周围3 km内无大型化工厂、垃圾场以及矿厂等。  \n（4）禁止建场区域为生活饮用水源、食品厂上游，以免造成污染；禁止建设在风景名胜区、自然保护区以及国家或地方法律与法规不允许的区域内。"
                ],
                "generated_answer": "乌骨鸡适合生长的环境包括：  \n1. 高而通风、阳光充足、冬暖夏凉不积水的小山丘，方向以坐北朝南为好。  \n2. 林地、草坡、果园等，要求地势平坦的高地或缓坡，排水良好、采光充足、通风良好，树林遮荫度在70%以上。  \n3. 隔离条件好，距离干线公路、其他畜禽场、村镇以及居民点至少1 km以上。  \n4. 远离污染源，周围3 km内无大型化工厂、垃圾场及矿厂等。  \n5. 舍内温度适宜为16～25℃，需注意夏季降温、冬季保温。",
                "latency_ms": 2293.674699998519,
                "timestamp": "2026-05-05 21:54:19",
                "user_rating": 0,
                "expert_corrected_answer": null,
                "expert_corrected_queries": null,
                "status": "normal",
                "llm_judge_score": 5,
                "llm_judge_reason": "回答完全基于提供的上下文，准确描述了乌骨鸡适合的生长环境，包括场地选择、温度要求和隔离条件，没有添加任何幻觉内容。",
                "qa_metadata": {
                    "mode": "multi_branch_retrieval",
                    "sources": [
                        "生态型乌骨鸡养殖技术_韩慧英.md",
                        "乌骨鸡饲养_卢祖生.md",
                        "乌骨鸡生态健康养殖技术_张朝廷.md",
                        "致富门路.md",
                        "赤水乌骨鸡林下生态养殖技术_赵其钞.md"
                    ],
                    "session_id": "80fc06acdfbc4db6b85e025da9334f59",
                    "retrieval_debug": [
                        {
                            "k": 12,
                            "query": "乌骨鸡适合什么环境生长",
                            "branch": "primary_dense",
                            "weight": 1.0,
                            "raw_hits": 12,
                            "max_score": 0.4365562796592712,
                            "filter_used": false,
                            "filter_candidates": 0
                        },
                        {
                            "k": 8,
                            "query": "么环境 乌骨鸡 什么环 合什么 境生长 环境生",
                            "branch": "keyword_dense",
                            "weight": 0.42,
                            "raw_hits": 8,
                            "max_score": 0.4375231862068176,
                            "filter_used": false,
                            "filter_candidates": 0
                        }
                    ],
                    "retrieval_max_score": 0.4375231862068176,
                    "session_memory_enabled": true
                },
                "expert_diagnosis": null,
                "expert_medication_advice": null,
                "review_reason": null,
                "kb_sync_status": null,
                "kb_sync_error": null,
                "kb_synced_at": null,
                "created_at": "2026-05-05 21:54:19",
                "updated_at": "2026-05-05 21:54:20"
            },
            {
                "id": "9600823a564b4b899a4a94080121d35f",
                "user_id": 48,
                "user_query": "乌骨鸡什么味道的",
                "rewritten_queries": [
                    "么味道 乌骨鸡 什么味 味道的 骨鸡什 鸡什么"
                ],
                "retrieved_contexts": [
                    "# 1 乌骨鸡概况",
                    "# 四川山地乌骨鸡",
                    "# 盐津乌骨鸡简介",
                    "# 一、白色丝毛乌骨鸡",
                    "乌骨鸡为雉科动物，为家鸡的一种，一般羽毛为白色，其皮、 肉、骨、 嘴皆为乌色，也有黑毛乌骨的，斑毛乌骨的和肉白乌骨的。 乌骨鸡全身都是宝，其肉、蛋具有较好的营养和药用价值，是上等的疗病滋补佳品。"
                ],
                "generated_answer": "根据现有信息，没有直接描述乌骨鸡的味道。但可以知道的是，乌骨鸡的肉和蛋具有较好的营养和药用价值，是上等的疗病滋补佳品。具体味道可能因饲养环境、饲料等因素而有所不同。",
                "latency_ms": 1542.5169999980426,
                "timestamp": "2026-05-05 22:00:57",
                "user_rating": 0,
                "expert_corrected_answer": null,
                "expert_corrected_queries": null,
                "status": "normal",
                "llm_judge_score": 4,
                "llm_judge_reason": "回答准确指出没有直接描述乌骨鸡的味道，同时引用了上下文中的营养和药用价值信息，相关性高且无幻觉。但未进一步推测或说明可能的风味特点，略显简略。",
                "qa_metadata": {
                    "mode": "multi_branch_retrieval",
                    "sources": [
                        "不同饲养方式对乌骨鸡生产性...品营养及药物残留的影响研究_左丽娟.md",
                        "四川山地乌骨鸡_申洪兵.md",
                        "盐津乌骨鸡简介_杨家义.md",
                        "介绍两种乌鸡优良品种.md",
                        "乌骨鸡的药用价值及食疗方法_金江.md"
                    ],
                    "session_id": "80fc06acdfbc4db6b85e025da9334f59",
                    "retrieval_debug": [
                        {
                            "k": 12,
                            "query": "乌骨鸡什么味道的",
                            "branch": "primary_dense",
                            "weight": 1.0,
                            "raw_hits": 12,
                            "max_score": 0.5009706616401672,
                            "filter_used": false,
                            "filter_candidates": 0
                        },
                        {
                            "k": 8,
                            "query": "么味道 乌骨鸡 什么味 味道的 骨鸡什 鸡什么",
                            "branch": "keyword_dense",
                            "weight": 0.42,
                            "raw_hits": 8,
                            "max_score": 0.54908686876297,
                            "filter_used": false,
                            "filter_candidates": 0
                        }
                    ],
                    "retrieval_max_score": 0.54908686876297,
                    "session_memory_enabled": true
                },
                "expert_diagnosis": null,
                "expert_medication_advice": null,
                "review_reason": null,
                "kb_sync_status": null,
                "kb_sync_error": null,
                "kb_synced_at": null,
                "created_at": "2026-05-05 22:00:57",
                "updated_at": "2026-05-05 22:00:58"
            },
            {
                "id": "11019319d8d44efc9be1315740d37cfb",
                "user_id": 48,
                "user_query": "hello",
                "rewritten_queries": [],
                "retrieved_contexts": [
                    "# 报 道",
                    "!",
                    "#",
                    "#",
                    "#"
                ],
                "generated_answer": "你好！有什么家禽养殖方面的问题需要咨询吗？",
                "latency_ms": 965.9294000011869,
                "timestamp": "2026-05-05 22:01:59",
                "user_rating": 0,
                "expert_corrected_answer": null,
                "expert_corrected_queries": null,
                "status": "normal",
                "llm_judge_score": 5,
                "llm_judge_reason": "回答完全基于上下文，简洁且直接回应了用户问候，没有编造内容，相关性高。",
                "qa_metadata": {
                    "mode": "multi_branch_retrieval",
                    "sources": [
                        "胸腺提取液喂鸡的试验研究_王兴金.md",
                        "中华乌鸡的药用价值_李瑞生.md",
                        "乌蒙乌骨鸡Myf5基因多态性与生长体重的关联分析_王文涛.md",
                        "BCO2在黄色和白色皮肤鸡种中突变位点筛选_甘师仪.md",
                        "江山的旅游与美食_毛平.md"
                    ],
                    "session_id": "80fc06acdfbc4db6b85e025da9334f59",
                    "retrieval_debug": [
                        {
                            "k": 12,
                            "query": "hello",
                            "branch": "primary_dense",
                            "weight": 1.0,
                            "raw_hits": 12,
                            "max_score": 0.8139363527297974,
                            "filter_used": false,
                            "filter_candidates": 0
                        }
                    ],
                    "retrieval_max_score": 0.8139363527297974,
                    "session_memory_enabled": true
                },
                "expert_diagnosis": null,
                "expert_medication_advice": null,
                "review_reason": null,
                "kb_sync_status": null,
                "kb_sync_error": null,
                "kb_synced_at": null,
                "created_at": "2026-05-05 22:01:59",
                "updated_at": "2026-05-05 22:02:00"
            },
            {
                "id": "6d6b6a4f50fe4f158db194280cb06b07",
                "user_id": 48,
                "user_query": "你好啊",
                "rewritten_queries": [],
                "retrieved_contexts": [
                    "# 报 道",
                    "# 招聘岗位",
                    "。",
                    "。",
                    "# 清楚。"
                ],
                "generated_answer": "你好！有什么家禽养殖方面的问题需要咨询吗？",
                "latency_ms": 904.6130000024277,
                "timestamp": "2026-05-05 22:08:46",
                "user_rating": 0,
                "expert_corrected_answer": null,
                "expert_corrected_queries": null,
                "status": "normal",
                "llm_judge_score": 5,
                "llm_judge_reason": "回答简洁且完全基于上下文，没有编造内容，直接回应了用户问候，并引导其提出具体问题，符合要求。",
                "qa_metadata": {
                    "mode": "multi_branch_retrieval",
                    "sources": [
                        "胸腺提取液喂鸡的试验研究_王兴金.md",
                        "乌鸡黑色素的提取和测定及黑...过度沉积相关基因的研究进展_蒋明.md",
                        "淅乌骨鸡群体遗传多样性及绿壳蛋性状相关基因SNP分析_王帅豪.md",
                        "西畴乌骨鸡乌质性状评价标准建立及鸡冠乌度转录组学研究_康佳佳.md",
                        "鸡FATP4基因cDNA的...生物信息学分析及其组织表达_杨岭.md"
                    ],
                    "session_id": "80fc06acdfbc4db6b85e025da9334f59",
                    "retrieval_debug": [
                        {
                            "k": 12,
                            "query": "你好啊",
                            "branch": "primary_dense",
                            "weight": 1.0,
                            "raw_hits": 12,
                            "max_score": 0.8537145256996155,
                            "filter_used": false,
                            "filter_candidates": 0
                        }
                    ],
                    "retrieval_max_score": 0.8537145256996155,
                    "session_memory_enabled": true
                },
                "expert_diagnosis": null,
                "expert_medication_advice": null,
                "review_reason": null,
                "kb_sync_status": null,
                "kb_sync_error": null,
                "kb_synced_at": null,
                "created_at": "2026-05-05 22:08:46",
                "updated_at": "2026-05-05 22:08:47"
            },
            {
                "id": "42611df917e5458594a36319515a5654",
                "user_id": 48,
                "user_query": "你好啊",
                "rewritten_queries": [],
                "retrieved_contexts": [
                    "# 报 道",
                    "# 招聘岗位",
                    "。",
                    "。",
                    "# 清楚。"
                ],
                "generated_answer": "你好！有什么家禽养殖方面的问题需要咨询吗？",
                "latency_ms": 1003.6108000022068,
                "timestamp": "2026-05-05 22:19:12",
                "user_rating": 0,
                "expert_corrected_answer": null,
                "expert_corrected_queries": null,
                "status": "normal",
                "llm_judge_score": 5,
                "llm_judge_reason": "回答简洁且完全基于上下文，没有编造内容，直接回应了用户问候，并引导其提出具体问题，相关性高。",
                "qa_metadata": {
                    "mode": "multi_branch_retrieval",
                    "sources": [
                        "胸腺提取液喂鸡的试验研究_王兴金.md",
                        "乌鸡黑色素的提取和测定及黑...过度沉积相关基因的研究进展_蒋明.md",
                        "淅乌骨鸡群体遗传多样性及绿壳蛋性状相关基因SNP分析_王帅豪.md",
                        "西畴乌骨鸡乌质性状评价标准建立及鸡冠乌度转录组学研究_康佳佳.md",
                        "鸡FATP4基因cDNA的...生物信息学分析及其组织表达_杨岭.md"
                    ],
                    "session_id": "80fc06acdfbc4db6b85e025da9334f59",
                    "retrieval_debug": [
                        {
                            "k": 12,
                            "query": "你好啊",
                            "branch": "primary_dense",
                            "weight": 1.0,
                            "raw_hits": 12,
                            "max_score": 0.8537145256996155,
                            "filter_used": false,
                            "filter_candidates": 0
                        }
                    ],
                    "retrieval_max_score": 0.8537145256996155,
                    "session_memory_enabled": true
                },
                "expert_diagnosis": null,
                "expert_medication_advice": null,
                "review_reason": null,
                "kb_sync_status": null,
                "kb_sync_error": null,
                "kb_synced_at": null,
                "created_at": "2026-05-05 22:19:12",
                "updated_at": "2026-05-05 22:19:14"
            },
            {
                "id": "11ae80661ecc4b2c9c35f6985b72897f",
                "user_id": 48,
                "user_query": "hello",
                "rewritten_queries": [],
                "retrieved_contexts": [
                    "# 报 道",
                    "!",
                    "#",
                    "#",
                    "#"
                ],
                "generated_answer": "你好！有什么家禽养殖方面的问题需要咨询吗？",
                "latency_ms": 1068.0511999998998,
                "timestamp": "2026-05-05 22:19:42",
                "user_rating": 0,
                "expert_corrected_answer": null,
                "expert_corrected_queries": null,
                "status": "normal",
                "llm_judge_score": 5,
                "llm_judge_reason": "回答完全基于上下文，简洁且直接回应了用户问候，并引导用户提出具体问题，符合家禽养殖领域的专业沟通方式。",
                "qa_metadata": {
                    "mode": "multi_branch_retrieval",
                    "sources": [
                        "胸腺提取液喂鸡的试验研究_王兴金.md",
                        "中华乌鸡的药用价值_李瑞生.md",
                        "乌蒙乌骨鸡Myf5基因多态性与生长体重的关联分析_王文涛.md",
                        "BCO2在黄色和白色皮肤鸡种中突变位点筛选_甘师仪.md",
                        "江山的旅游与美食_毛平.md"
                    ],
                    "session_id": "80fc06acdfbc4db6b85e025da9334f59",
                    "retrieval_debug": [
                        {
                            "k": 12,
                            "query": "hello",
                            "branch": "primary_dense",
                            "weight": 1.0,
                            "raw_hits": 12,
                            "max_score": 0.8139363527297974,
                            "filter_used": false,
                            "filter_candidates": 0
                        }
                    ],
                    "retrieval_max_score": 0.8139363527297974,
                    "session_memory_enabled": true
                },
                "expert_diagnosis": null,
                "expert_medication_advice": null,
                "review_reason": null,
                "kb_sync_status": null,
                "kb_sync_error": null,
                "kb_synced_at": null,
                "created_at": "2026-05-05 22:19:42",
                "updated_at": "2026-05-05 22:19:43"
            },
            {
                "id": "205ee43fae6a475488e072a8986d45b3",
                "user_id": 48,
                "user_query": "hellooooo",
                "rewritten_queries": [],
                "retrieved_contexts": [
                    "#",
                    "#",
                    "#",
                    "#",
                    "#"
                ],
                "generated_answer": "你好！有什么家禽养殖方面的问题需要咨询吗？",
                "latency_ms": 2847.130099999049,
                "timestamp": "2026-05-06 10:45:36",
                "user_rating": 0,
                "expert_corrected_answer": null,
                "expert_corrected_queries": null,
                "status": "normal",
                "llm_judge_score": 5,
                "llm_judge_reason": "回答完全基于上下文，简洁且准确地回应了用户的问题，没有出现幻觉或偏离主题的情况。",
                "qa_metadata": {
                    "mode": "multi_branch_retrieval",
                    "sources": [
                        "BCO2在黄色和白色皮肤鸡种中突变位点筛选_甘师仪.md",
                        "不同品种乌骨鸡主要性状指标及其杂交效果比较研究_王建.md",
                        "中外鸡种MHC_B-L基因...变异及与免疫性状关系的研究_刘立波.md",
                        "寻找乌骨鸡_盛永明.md",
                        "枸杞园放养乌骨鸡血液指标、肉食用品质及风味物质研究_袁君.md"
                    ],
                    "session_id": "80fc06acdfbc4db6b85e025da9334f59",
                    "retrieval_debug": [
                        {
                            "k": 12,
                            "query": "hellooooo",
                            "branch": "primary_dense",
                            "weight": 1.0,
                            "raw_hits": 12,
                            "max_score": 0.7802712917327881,
                            "filter_used": false,
                            "filter_candidates": 0
                        }
                    ],
                    "retrieval_max_score": 0.7802712917327881,
                    "session_memory_enabled": true
                },
                "expert_diagnosis": null,
                "expert_medication_advice": null,
                "review_reason": null,
                "kb_sync_status": null,
                "kb_sync_error": null,
                "kb_synced_at": null,
                "created_at": "2026-05-06 10:45:36",
                "updated_at": "2026-05-06 10:45:41"
            },
            {
                "id": "19840c596df644ac9ae06088a1605ffa",
                "user_id": 48,
                "user_query": "hhhhh",
                "rewritten_queries": [],
                "retrieved_contexts": [
                    "# !! \"",
                    "# !",
                    "# # !\"#\\$%",
                    "# # !\"#\\$%",
                    "#"
                ],
                "generated_answer": "你好！有什么家禽养殖方面的问题需要咨询吗？",
                "latency_ms": 1976.5641000012693,
                "timestamp": "2026-05-06 11:23:47",
                "user_rating": 0,
                "expert_corrected_answer": null,
                "expert_corrected_queries": null,
                "status": "normal",
                "llm_judge_score": 1,
                "llm_judge_reason": "回答完全偏离用户问题，用户输入的是无意义字符串 'hhhhh'，而系统回复了无关的问候语，未提供任何有用信息。",
                "qa_metadata": {
                    "mode": "multi_branch_retrieval",
                    "sources": [
                        "枸杞园放养乌骨鸡肉食用品质与血清生化指标相关性分析_袁君.md",
                        "浅析乌鸡的药用及保健价值_王晓通.md",
                        "孵化期间乌骨鸡种蛋水分和钙变化及胚胎生长的研究_房兴堂.md",
                        "乌蒙乌骨鸡Myf5基因多态性与生长体重的关联分析_王文涛.md"
                    ],
                    "session_id": "80fc06acdfbc4db6b85e025da9334f59",
                    "retrieval_debug": [
                        {
                            "k": 12,
                            "query": "hhhhh",
                            "branch": "primary_dense",
                            "weight": 1.0,
                            "raw_hits": 12,
                            "max_score": 0.7942354679107666,
                            "filter_used": false,
                            "filter_candidates": 0
                        }
                    ],
                    "retrieval_max_score": 0.7942354679107666,
                    "session_memory_enabled": true
                },
                "expert_diagnosis": null,
                "expert_medication_advice": null,
                "review_reason": null,
                "kb_sync_status": null,
                "kb_sync_error": null,
                "kb_synced_at": null,
                "created_at": "2026-05-06 11:23:47",
                "updated_at": "2026-05-06 11:23:48"
            },
            {
                "id": "26133a11eecb4f3eb4719bb75df00340",
                "user_id": 48,
                "user_query": "你很会养鸡吗",
                "rewritten_queries": [
                    "会养鸡 你很会 养鸡吗 很会养 会养 你很"
                ],
                "retrieved_contexts": [
                    "# 3掌握好饲养密度  \n密度过大，鸡的活动采食受限制，生长慢，发育不齐，空气流通差，易发生啄癖，死亡率高；密度过小则浪费人力物力。一般平面饲养每平方米1一2周龄50羽，3—5周龄30羽，6-8周龄20羽，9-13周龄10羽。此外还应注意鸡群数量不宜太大，以每群饲养200－300羽为宜。",
                    "# 营养需要  \n成 年 鸡 的 营 养 需 要 见 表 ， 在 按 表 配 制  \n日粮时也需添加维生素及微量元素添加剂。  \n度年劝的， 共\n![](images/cdd712a644c7ae5a06f55d689f49a6903a55aeaf595f6810f1d57ac375c5cfc4.jpg)",
                    "# 2 场地的选择  \n一般要选择丘陵 山地 果场 高山半山腰 山顶等 场地最好能与农作物有一段距离 以免鸡损坏农作物 如果离农作物较近 则要用围网把农作物围起来 饲养地点最好选择能用水管引得到山泉为优以减轻供水的劳动力。如果有机耕路到达则最好 可以方便饲料和鸡的运输 场地的树林 灌木、树叶、杂草不必清除可以供鸡采食还可以遮荫。可以适当饲养几条家狗 一可以驱除天敌 二可以防人偷鸡 鸡在多个场地进行轮流放养每个场地应有一定的距离以防疫病交叉感染 给场地一定时间的休养期 可以利用阳光 雨水等自然条件消除病菌 还可以有利于植被 杂草的生长和恢复",
                    "# 5.2搞好清洁卫生  \n鸡场内外，鸡舍内外必须经常清扫，食槽、饮水器、生产工具要定期洗刷、消毒。每天定时清粪，定期灭鼠、杀虫，消除疾病的传摇媒介，及时清除垃圾，消灭蚊蝇，及时消除死鸡，隔离病鸡。",
                    "# 三、科学饲养  \n饲养 舍饲 因地制宜建造一个冬暖以上 夏凉 以下 高燥 通风良好 坐北朝南的鸡舍。舍前设运动场每平方米不超过4只。（2）放牧：防疫条件好的可放牧饲养 笼养 同大批量养蛋鸡一样但鸡舍造价和设备投资相对高些。（4）利用塑料大棚饲养 夏天炎热 可在棚顶加盖草帘  \n2．饲料 根据用途不同选择相应的饲料。料量 周龄 每只日饲料消耗量 克 周龄 克周龄 克 周龄 克 周龄 克 周龄 克周龄50克成鸡60克左右。（1）选择蛋鸡全价料从雏鸡开始选择料号饲喂 肉用 选择肉鸡全价料日龄～30日龄雏鸡全价料；30日龄～60日龄中雏生长料；60日龄～120日龄育肥全价料。（3）药用：选择特制的中草药做为添加剂 于 日龄后添加 直到出栏。  \n3．繁育 （1）机械孵化：适用于大中型养鸡场。养户可购置小型孵化器或电孵化箱 火炕孵化 在火炕上铺可控温的电褥子其上面放热水袋后加盖棉被 温度保持在 传统孵化 可用就巢鸡孵化  \n（061001 河北沧州农校）"
                ],
                "generated_answer": "我具备一定的家禽养殖知识，可以为你提供相关指导。如果你有具体问题，欢迎随时提问！",
                "latency_ms": 1866.9961000000512,
                "timestamp": "2026-05-06 16:02:44",
                "user_rating": 0,
                "expert_corrected_answer": null,
                "expert_corrected_queries": null,
                "status": "normal",
                "llm_judge_score": 3,
                "llm_judge_reason": "回答较为笼统，虽然没有明显幻觉，但未直接展示具体养殖知识，也未引用上下文中的具体内容，相关性一般。",
                "qa_metadata": {
                    "mode": "multi_branch_retrieval",
                    "sources": [
                        "商品乌骨鸡饲养管理技术要点_吴光程.md",
                        "乌骨鸡的饲养管理技术_康相涛.md",
                        "西畴山地乌骨鸡的饲养管理_陆灵勇.md",
                        "乌鸡的规模化养殖技术_汪莉.md",
                        "科学饲养乌骨鸡_孙庆伟.md"
                    ],
                    "session_id": "80fc06acdfbc4db6b85e025da9334f59",
                    "retrieval_debug": [
                        {
                            "k": 12,
                            "query": "你很会养鸡吗",
                            "branch": "primary_dense",
                            "weight": 1.0,
                            "raw_hits": 12,
                            "max_score": 0.6874305009841919,
                            "filter_used": false,
                            "filter_candidates": 0
                        },
                        {
                            "k": 8,
                            "query": "会养鸡 你很会 养鸡吗 很会养 会养 你很",
                            "branch": "keyword_dense",
                            "weight": 0.42,
                            "raw_hits": 8,
                            "max_score": 0.7068487405776978,
                            "filter_used": false,
                            "filter_candidates": 0
                        }
                    ],
                    "retrieval_max_score": 0.7068487405776978,
                    "session_memory_enabled": true
                },
                "expert_diagnosis": null,
                "expert_medication_advice": null,
                "review_reason": null,
                "kb_sync_status": null,
                "kb_sync_error": null,
                "kb_synced_at": null,
                "created_at": "2026-05-06 16:02:44",
                "updated_at": "2026-05-06 16:02:46"
            },
            {
                "id": "623c4d331a3c4115bb552e24e560d7c7",
                "user_id": 48,
                "user_query": "乌骨鸡生病了怎么办",
                "rewritten_queries": [
                    "乌骨鸡 了怎么 怎么办 生病了 病了怎 骨鸡生"
                ],
                "retrieved_contexts": [
                    "防止应激。 乌骨鸡胆小怕惊， 对各种应激敏感性特强 ， 因此 除提供一 个安静的饲养环境外 工 作时还应注意做到轻、 慢 防止应激发生。  \n劫观察。 乌骨鸡对外界抵抗能力差， 易发生各种疾 病。 因此应勤观察 发现离群呆立 精神不好 不吃 食或粪便异常的鸡， 应及时隔离治疗 加强护理。  \n疾病防治。 为了保证乌骨鸡的健康生长 减少经济损失 必 须做好疾病防治 坚持无病早防 有病早治的原则。 鸡舍门口 设消毒池 ， 禁止 外人 参观 ， 做好育雏舍及 用 具 的清洁卫生与消毒。 根据 乌骨鸡发病规律， 进行合理 投 药。 育雏的前 周内， 应 在饲 料或饮水中添 加抗生素 如土霉素、 氯霉素、 痢特灵 等药物 ， 预防鸡白痢和大肠杆菌病 第 周起在饲料中添 加 抗 球 虫 药 ， 如 球 虫 灵 、 氯 苯 版 、 克球 粉 等 防 止 球虫病发 生 与此 同时 还 崖根 据本地疫 病流 行情 况制定科学的疫苗免疫程 序， 适时地进行 各种免疫注射 以防各种疫病的发生流行。",
                    "此 外 大 肠 杆 菌 病 、 禽 霍 乱 、 啄 癖 等 也 有 发 生 。 因此， 要 保证乌骨鸡 的健康生 长， 提高成活率， 减少 经济损失， 必 须做好各种疫病的综合防制， 坚持无病早 防、有 病 早 治 的 原 则 。 鸡 群 实 行 “ 全 进 全 出 ” 制 ， 鸡 舍 门 口设 消毒池， 禁止 外来人 员参观， 并做好鸡舍及 用具 的清洁卫 生与消毒， 防止 雏鸡早期 感染。 加 强饲养管理，提 供优质全 价饲 料， 减少各种应激， 搞好 鸡舍通 风 换气， 勤换垫 料， 减少舍 内氨气 等有害气体， 改善鸡 群 生活环境。 根据乌骨鸡的发病规律 进行合理 用药， 育雏的前 周 内， 应在饲料或饮水 中添 加抗 菌 药物， 如土母素、 抓霉素、 痢特灵 等， 预 防鸡 白痢 杆菌病和 大肠 杆菌病 第 周起 至 日龄 在饲 料 中添 加 抓苯肌 克球粉、 抗球 王等抗球虫药， 预 防鸡球虫病 在 呼 吸道病 的多发季节， 应常用链雄素、 红霉 素等药物 预 防。 应根据本地疫病流行情况， 制定科学 的免疫 程序， 适 时地进行各种免 疫接种， 以 防各种疫病 的发 生与流行。 豆",
                    "# 1. 卫生防疫工作  \n乌骨鸡抗病力较差 所以要特别强调卫生防疫工作 饲养前鸡舍内外应用 烧碱水彻底消毒 墙壁用 石灰水刷白 每半个月用 百毒杀消毒 次 垫草要勤换 食槽饮水器应每天洗刷干净 清扫出的粪便 垫草要远离鸡舍作无公害处理 周龄内雏鸡,每天应在饲料中加入适量痢特灵防止副伤寒和白痢病。2周龄后加适量痢特灵 停 天后再喂 次 周龄后要防治蛔虫病 另外 周龄时还要注射禽流感和鸡新城疫疫苗",
                    "# 二、疾病防治  \n乌骨鸡体质娇弱 抗病力较差 必须搞好疾病的防治工作 首先 要搞好饮水和饲料卫生 经常打扫鸡舍和运动场定期消毒。其次建立健全防疫制度及时进行鸡新城疫、禽霍乱等疫苗的防疫注射和药物预防特别要注意对马立克氏病和硬嗉病两个易发病的防治。",
                    "乌骨鸡的抗病力较其他鸡差，易受各种疾病的威胁，要养好乌骨鸡，疾病防治也很关键。目前，为害乌骨鸡的疾病有：新城疫、球虫病，鸡白痢，马立克氏病、鸡法氏囊病和鸡痘等，具体的预防办法和预防程序与普通产蛋鸡大致相同。"
                ],
                "generated_answer": "如果乌骨鸡生病了，可以采取以下措施：  \n1. **及时隔离病鸡**，避免疾病传播。  \n2. **观察症状**，如精神不好、不吃食、粪便异常等，根据症状对症处理。  \n3. **保持环境清洁**，加强鸡舍消毒，勤换垫料，改善通风条件。  \n4. **合理用药**，根据疾病类型在饲料或饮水中添加抗生素或抗球虫药（如土霉素、痢特灵、球虫灵等）。  \n5. **做好疫苗免疫**，根据当地疫情进行新城疫、禽流感等疫苗接种。  \n\n建议根据具体病情咨询专业兽医进行针对性治疗。",
                "latency_ms": 6804.834099999425,
                "timestamp": "2026-05-06 22:19:59",
                "user_rating": 0,
                "expert_corrected_answer": null,
                "expert_corrected_queries": null,
                "status": "normal",
                "llm_judge_score": 5,
                "llm_judge_reason": "回答完全基于提供的上下文，准确列出了隔离病鸡、观察症状、环境清洁、合理用药和疫苗免疫等措施，没有编造内容，且直接解决了用户问题。",
                "qa_metadata": {
                    "mode": "multi_branch_retrieval",
                    "sources": [
                        "提高乌骨鸡育雏成活率的八项技术措施_吴光程.md",
                        "乌骨鸡五大疫病的防制_吴光程.md",
                        "乌骨鸡的科学养殖_洪森旗.md",
                        "乌骨鸡的开发利用_李建鑫.md",
                        "乌骨鸡及其饲养技术_王长生.md"
                    ],
                    "session_id": "80fc06acdfbc4db6b85e025da9334f59",
                    "retrieval_debug": [
                        {
                            "k": 12,
                            "query": "乌骨鸡生病了怎么办",
                            "branch": "primary_dense",
                            "weight": 1.0,
                            "raw_hits": 12,
                            "max_score": 0.368893563747406,
                            "filter_used": false,
                            "filter_candidates": 0
                        },
                        {
                            "k": 8,
                            "query": "乌骨鸡 了怎么 怎么办 生病了 病了怎 骨鸡生",
                            "branch": "keyword_dense",
                            "weight": 0.42,
                            "raw_hits": 8,
                            "max_score": 0.3802187144756317,
                            "filter_used": false,
                            "filter_candidates": 0
                        }
                    ],
                    "retrieval_max_score": 0.3802187144756317,
                    "session_memory_enabled": true
                },
                "expert_diagnosis": null,
                "expert_medication_advice": null,
                "review_reason": null,
                "kb_sync_status": null,
                "kb_sync_error": null,
                "kb_synced_at": null,
                "created_at": "2026-05-06 22:19:59",
                "updated_at": "2026-05-06 22:20:01"
            },
            {
                "id": "9d178fe75b734388be25156ee03cb28d",
                "user_id": 48,
                "user_query": "乌骨鸡长什么样",
                "rewritten_queries": [
                    "乌骨鸡 什么样 长什么 骨鸡长 鸡长什 么样"
                ],
                "retrieved_contexts": [
                    "# 乌骨鸡的外貌特征与生活习性",
                    "# 一、 外貌特征  \n纯种乌骨鸡头小、 颈短、 遍体羽 毛 白色， 呈丝绒状 身体轻小，性情温驯， 行动迟缓 ， 外貌奇特美观。  \n概括起来有九大特征， 即续头、 丝冠、 绿耳、 胡须、 丝毛、 五爪、 毛腿、 乌骨、 乌皮。 无论公母鸡 ， 其眼睛、 咏、趾、 为黑色， 内脏与脂肪亦带黑色。 除主翼羽 、 副翼羽和部分公鸡的尾羽有分裂的羽 片外， 其余全身为绒羽 ， 每一羽 根可长 出 多支绒羽 ，护都的 羽 毛长至第一趾。 成年公鸡平均体重为 · ， 母鸡为。 成年乌骨鸡体尺测量见表  \n表 泰和乌骨鸡成鸡体尺表 单位\n![](images/6476b8f2d47ca063d26cefa32525c55f75514c14139d8a6ba141b95cf0fc4711.jpg)",
                    "# 1.1.1乌骨鸡的外貌特征  \n从观赏型角度来评价，传统标准的乌骨鸡具有丝毛、缨头、凤冠、绿耳、胡须、毛腿、五爪、乌皮、乌肉、乌骨的\"十全\"特征；从商品生产角度出发，主要为肉用，按体型大小分为大、中、小三型；依据羽毛颜色划分，有白色、黑色、杂色三型；按羽毛类型分，有丝羽、片羽、卷羽三类[5]。",
                    "# 1乌骨鸡的外貌特征和性能  \n纯种乌骨鸡头小，颈短，遍体 羽毛白色，呈丝绒状。身体轻小，性情温驷，行动迟缓，外貌奇特美观。概括起来有十大特征：①缨头。头顶有一小摄较长的丝毛，形如白绒球，形成毛冠。②丝冠。又称玫瑰冠，呈紫黑色，又如桑椹状，母鸡冠小，公鸡冠大。③绿耳。耳叶蓝中透绿，呈孔雀藍色，在性成熟初期更鲜艳夺目。④胡须。从耳根到两颈、下颌有较长的细毛，形同挂须，母鸡发达。③丝毛。全身羽毛洁白，除两翅主翼和尾羽的箭形羽毛外，全呈绒状细毛。⑥五爪。即脚上有五趾。③毛腿。从部到脚趾基部密生白毛。⑧乌骨。骨质骨膜均暗乌。③乌肉。肌肉、内脏及脂肪均墨乌黑色。①乌皮。开身皮肤及眼、喙、爪均为黑色，由此而得名乌骨鸡。但生产实践中区别真假纯种乌骨鸟是看乌骨鸡的鸡舌，鸡舌乌黑（浅乌）者，才是品质优良的岛骨鸡，就是“观鸡舌黑者，则肉骨具乌”。  \n乌骨鸡的初生体重平均为26.9g，成年公鸡体重1.2～1.6kg，平均160天开啼；母鸡1.0～12.5kg，160～180天开产，年平均产蛋80～150枚，蛋重40～46g，蛋壳棕褐色，母鸡就巢性强，每产约30枚蛋就停产“抱窝”一次，每次持续时间30天以上，每年“抱窝”6～7次。",
                    "# 二 、 体型外貌  \n山地 乌骨鸡 以冠、 嚎、 髯、 舌 、 皮、 肉、 骨及 内脏含油 乌黑为主要特征 ， 尤乌皮、 乌 肉、 乌骨“ 三乌”羽色以 紫兰色黑毛居多 ， 斑毛及 白毛次之 羽型 以常羽为主 ， 反羽 狗毛 和丝羽 次之。  \n公鸡体格高大 ， 母鸡体格适 中 相对“ 丝羽 乌骨鸡” 为大 型 品种 ， 躯体发育匀称， 体型 轮廊近似“ 砖型 ” 、 “ 船 型 ” 、 “ 方 型 ” 。 山 地 乌 骨 鸡 成 年 体 重 公 鸡 平 均一 公斤 有的可达 公斤 ， 母鸡 一公斤 有的可达 公斤 。"
                ],
                "generated_answer": "乌骨鸡的外貌特征如下：\n\n- 头小、颈短，全身羽毛白色，呈丝绒状。\n- 具有九大特征：缨头（头顶有白绒球）、丝冠（紫黑色玫瑰冠）、绿耳（蓝中透绿的耳叶）、胡须（下颌有细长毛）、丝毛（全身绒状细毛）、五爪（脚上有五趾）、毛腿（从腿部到脚趾基部密生白毛）、乌骨（骨质和骨膜乌黑）、乌皮（皮肤、眼、喙、爪均为黑色）。\n- 除主翼羽、副翼羽和部分公鸡的尾羽外，全身为绒羽。\n- 成年公鸡体重约1.2～1.6公斤，母鸡约1.0～1.25公斤。",
                "latency_ms": 36508.20109999768,
                "timestamp": "2026-05-06 23:10:36",
                "user_rating": 0,
                "expert_corrected_answer": null,
                "expert_corrected_queries": null,
                "status": "normal",
                "llm_judge_score": 5,
                "llm_judge_reason": "回答完全基于检索到的上下文，准确描述了乌骨鸡的外貌特征，包括九大特征、羽毛类型、体重等信息，没有编造内容，且直接回答了用户问题。",
                "qa_metadata": {
                    "mode": "multi_branch_retrieval",
                    "sources": [
                        "乌骨鸡的饲养_张宏伟.md",
                        "泰和乌骨鸡生产性能观察_王纪亭.md",
                        "不同品种乌骨鸡主要性状指标及其杂交效果比较研究_王建.md",
                        "乌骨鸡及其饲养技术_王长生.md",
                        "山地乌骨鸡高产饲养技术研究...地乌骨鸡品种综述及开发现状_王成.md"
                    ],
                    "session_id": "80fc06acdfbc4db6b85e025da9334f59",
                    "retrieval_debug": [
                        {
                            "k": 12,
                            "query": "乌骨鸡长什么样",
                            "branch": "primary_dense",
                            "weight": 1.0,
                            "raw_hits": 12,
                            "max_score": 0.3839451670646667,
                            "filter_used": false,
                            "filter_candidates": 0
                        },
                        {
                            "k": 8,
                            "query": "乌骨鸡 什么样 长什么 骨鸡长 鸡长什 么样",
                            "branch": "keyword_dense",
                            "weight": 0.42,
                            "raw_hits": 8,
                            "max_score": 0.3658551573753357,
                            "filter_used": false,
                            "filter_candidates": 0
                        }
                    ],
                    "retrieval_max_score": 0.3839451670646667,
                    "session_memory_enabled": true
                },
                "expert_diagnosis": null,
                "expert_medication_advice": null,
                "review_reason": null,
                "kb_sync_status": null,
                "kb_sync_error": null,
                "kb_synced_at": null,
                "created_at": "2026-05-06 23:10:36",
                "updated_at": "2026-05-06 23:11:01"
            }
        ]
    }
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» code|integer|true|none||none|
|» message|string|true|none||none|
|» data|object|true|none||none|
|»» ok|boolean|true|none||none|
|»» data|[object]|true|none||none|
|»»» id|string|true|none||none|
|»»» user_id|integer|true|none||none|
|»»» user_query|string|true|none||none|
|»»» rewritten_queries|[string]|true|none||none|
|»»» retrieved_contexts|[string]|true|none||none|
|»»» generated_answer|string|true|none||none|
|»»» latency_ms|number|true|none||none|
|»»» timestamp|string|true|none||none|
|»»» user_rating|integer|true|none||none|
|»»» expert_corrected_answer|null|true|none||none|
|»»» expert_corrected_queries|null|true|none||none|
|»»» status|string|true|none||none|
|»»» llm_judge_score|integer|true|none||none|
|»»» llm_judge_reason|string|true|none||none|
|»»» qa_metadata|object|true|none||none|
|»»»» mode|string|true|none||none|
|»»»» sources|[string]|true|none||none|
|»»»» session_id|string|true|none||none|
|»»»» retrieval_debug|[object]|true|none||none|
|»»»»» k|integer|true|none||none|
|»»»»» query|string|true|none||none|
|»»»»» branch|string|true|none||none|
|»»»»» weight|integer|true|none||none|
|»»»»» raw_hits|integer|true|none||none|
|»»»»» max_score|number|true|none||none|
|»»»»» filter_used|boolean|true|none||none|
|»»»»» filter_candidates|integer|true|none||none|
|»»»» retrieval_max_score|number|true|none||none|
|»»»» session_memory_enabled|boolean|true|none||none|
|»»» expert_diagnosis|null|true|none||none|
|»»» expert_medication_advice|null|true|none||none|
|»»» review_reason|null|true|none||none|
|»»» kb_sync_status|null|true|none||none|
|»»» kb_sync_error|null|true|none||none|
|»»» kb_synced_at|null|true|none||none|
|»»» created_at|string|true|none||none|
|»»» updated_at|string|true|none||none|

## POST chatStream

POST /api/ai/chat/stream

> Body 请求参数

```json
{"query":"乌骨鸡长什么样","session_id":"80fc06acdfbc4db6b85e025da9334f59"}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|Content-Type|header|string| 是 |none|
|satoken|header|string| 是 |none|
|Cookie|header|string| 是 |none|
|body|body|object| 是 |none|
|» query|body|string| 是 |none|
|» session_id|body|string| 是 |none|

> 返回示例

> 200 Response

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

## POST feedback

POST /api/ai/feedback

> Body 请求参数

```json
{"log_id":"c85c39145b4a4776980aa2109fb26d33","rating":1}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|Content-Type|header|string| 是 |none|
|Pragma|header|string| 是 |none|
|satoken|header|string| 是 |none|
|Cookie|header|string| 是 |none|
|body|body|object| 是 |none|
|» log_id|body|string| 是 |none|
|» rating|body|integer| 是 |none|

> 返回示例

```json
{"code":200,"message":"ok","data":{"ok":true,"message":"Feedback recorded successfully"}}
```

```json
{
    "code": 200,
    "message": "ok",
    "data": {
        "ok": true,
        "message": "Feedback recorded successfully"
    }
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

# live

## POST 获取算法流

POST /api/live/ticket

> Body 请求参数

```json
{"deviceId":67}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|Content-Type|header|string| 是 |none|
|Pragma|header|string| 是 |none|
|satoken|header|string| 是 |none|
|Cookie|header|string| 是 |none|
|body|body|object| 是 |none|
|» deviceId|body|integer| 是 |none|

> 返回示例

```json
{"code":200,"message":"ok","data":{"deviceId":67,"streamId":"chicken138","wsUrl":"ws://124.71.97.178:8080/ws/live","ticket":"2910ca45b0b840778afcb514dd13fea3","expiresAt":"2026-05-23T16:46:58.736824916+08:00"}}
```

```json
{
    "code": 200,
    "message": "ok",
    "data": {
        "deviceId": 67,
        "streamId": "chicken138",
        "wsUrl": "ws://124.71.97.178:8080/ws/live",
        "ticket": "ef7999cfda0741abbe16bb87a165f26a",
        "expiresAt": "2026-05-23T16:47:19.693791567+08:00"
    }
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

# 数据模型

