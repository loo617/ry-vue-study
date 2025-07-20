# 阿里云录音文件识别 API 集成

本项目基于 RuoYi-Vue-Plus 框架，使用 Spring Boot RestClient 接入阿里云录音文件识别闲时版接口，支持重试和超时配置。

## 功能特性

-   ✅ 基于 RuoYi-Vue-Plus 框架和 Spring Boot 3.4.7
-   ✅ 使用框架统一的响应格式`R<T>`
-   ✅ 集成 Sa-Token 权限认证
-   ✅ 支持阿里云 API 签名认证
-   ✅ 配置化重试和超时机制
-   ✅ 完整的业务对象(BO)和视图对象(VO)封装
-   ✅ RESTful API 接口
-   ✅ 参数验证和统一异常处理
-   ✅ 详细的日志记录和操作审计
-   ✅ 单元测试覆盖

## 项目结构

```
ruoyi-api/
├── src/main/java/org/dromara/api/aliyun/isi/
│   ├── config/                 # 配置类
│   │   ├── AliyunIsiProperties.java
│   │   └── RestClientConfig.java
│   ├── controller/             # 控制器
│   │   └── IsiController.java
│   ├── domain/                # 领域对象
│   │   ├── bo/               # 业务对象
│   │   │   └── IsiTaskBo.java
│   │   └── vo/               # 视图对象
│   │       └── IsiTaskVo.java
│   ├── service/               # 服务层
│   │   ├── IAliyunIsiService.java
│   │   └── impl/
│   │       └── AliyunIsiServiceImpl.java
│   └── util/                  # 工具类
│       ├── AliyunSignatureUtil.java
│       └── RetryUtil.java
├── src/main/resources/
│   └── application.yml        # 配置文件
└── src/test/java/            # 测试代码
    └── org/dromara/api/aliyun/isi/service/
        └── AliyunIsiServiceTest.java
```

## 快速开始

### 1. 配置阿里云访问密钥

在 `application.yml` 中配置您的阿里云访问密钥：

```yaml
aliyun:
    isi:
        access-key-id: ${ALIYUN_ACCESS_KEY_ID:your-access-key-id}
        access-key-secret: ${ALIYUN_ACCESS_KEY_SECRET:your-access-key-secret}
```

或者通过环境变量设置：

```bash
export ALIYUN_ACCESS_KEY_ID=your-access-key-id
export ALIYUN_ACCESS_KEY_SECRET=your-access-key-secret
```

### 2. 启动应用

```bash
mvn spring-boot:run
```

### 3. 使用 API

#### 提交录音文件识别任务

```bash
curl -X POST http://localhost:8080/api/isi/submit \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer your-token" \
  -d '{
    "fileLink": "https://example.com/audio.wav",
    "format": "wav",
    "duration": 60,
    "taskName": "测试任务",
    "enablePunctuation": true,
    "language": "zh"
  }'
```

#### 查询任务状态

```bash
curl -X GET http://localhost:8080/api/isi/query/{taskId} \
  -H "Authorization: Bearer your-token"
```

#### 健康检查

```bash
curl -X GET http://localhost:8080/api/isi/health
```

## 配置说明

### 阿里云录音文件识别配置

| 配置项              | 说明                  | 默认值                               |
| ------------------- | --------------------- | ------------------------------------ |
| `access-key-id`     | 阿里云访问密钥 ID     | -                                    |
| `access-key-secret` | 阿里云访问密钥 Secret | -                                    |
| `region-id`         | 阿里云区域 ID         | cn-shanghai                          |
| `endpoint`          | 录音文件识别服务端点  | https://isi.cn-shanghai.aliyuncs.com |
| `connect-timeout`   | 连接超时时间（毫秒）  | 10000                                |
| `read-timeout`      | 读取超时时间（毫秒）  | 30000                                |
| `max-retries`       | 最大重试次数          | 3                                    |
| `retry-interval`    | 重试间隔时间（毫秒）  | 1000                                 |

### Sa-Token 权限配置

| 权限标识         | 说明                     |
| ---------------- | ------------------------ |
| `api:isi:submit` | 提交录音文件识别任务     |
| `api:isi:query`  | 查询录音文件识别任务状态 |

### 支持的音频格式

-   WAV
-   MP3
-   M4A
-   FLAC
-   OGG

### 支持的语言

-   中文（zh）
-   英文（en）

## API 接口说明

### 提交识别任务

**接口地址：** `POST /api/isi/submit`

**权限要求：** `api:isi:submit`

**请求参数：**

| 参数名                   | 类型    | 必填 | 说明               |
| ------------------------ | ------- | ---- | ------------------ |
| fileLink                 | String  | 是   | 录音文件 URL       |
| format                   | String  | 是   | 录音文件格式       |
| duration                 | Integer | 是   | 录音文件时长（秒） |
| taskName                 | String  | 否   | 任务名称           |
| taskDescription          | String  | 否   | 任务描述           |
| callbackUrl              | String  | 否   | 回调 URL           |
| callbackParams           | String  | 否   | 回调参数           |
| enablePunctuation        | Boolean | 否   | 是否启用标点符号   |
| enableTimestamp          | Boolean | 否   | 是否启用时间戳     |
| enableSpeakerDiarization | Boolean | 否   | 是否启用说话人分离 |
| enableKeywordExtraction  | Boolean | 否   | 是否启用关键词识别 |
| language                 | String  | 否   | 语言类型           |
| sampleRate               | Integer | 否   | 音频采样率         |
| channelNum               | Integer | 否   | 音频声道数         |

**响应示例：**

```json
{
    "code": 200,
    "msg": "操作成功",
    "data": {
        "requestId": "12345678-1234-1234-1234-123456789012",
        "taskId": "task-12345678",
        "status": "SUBMITTED",
        "code": "200",
        "message": "success"
    }
}
```

### 查询任务状态

**接口地址：** `GET /api/isi/query/{taskId}`

**权限要求：** `api:isi:query`

**路径参数：**

| 参数名 | 类型   | 必填 | 说明    |
| ------ | ------ | ---- | ------- |
| taskId | String | 是   | 任务 ID |

**响应示例：**

```json
{
    "code": 200,
    "msg": "操作成功",
    "data": {
        "requestId": "12345678-1234-1234-1234-123456789012",
        "taskId": "task-12345678",
        "status": "SUCCEEDED",
        "code": "200",
        "message": "success",
        "result": {
            "text": "识别出的文本内容",
            "confidence": 0.95,
            "timestamps": "时间戳信息",
            "speakers": "说话人信息",
            "keywords": "关键词信息",
            "finishTime": "2024-01-01T12:00:00Z",
            "startTime": "2024-01-01T11:59:00Z"
        }
    }
}
```

## 错误处理

当 API 调用失败时，会返回统一的错误响应格式：

```json
{
    "code": 500,
    "msg": "错误描述信息",
    "data": null
}
```

常见错误码：

| 错误码                | 说明             |
| --------------------- | ---------------- |
| InvalidParameter      | 参数错误         |
| InvalidAccessKeyId    | 访问密钥 ID 无效 |
| SignatureDoesNotMatch | 签名不匹配       |
| TaskNotFound          | 任务不存在       |
| TaskFailed            | 任务执行失败     |

## 开发说明

### 框架特性

-   **统一响应格式**：使用`R<T>`类封装响应
-   **权限控制**：集成 Sa-Token 进行权限管理
-   **操作审计**：使用`@Log`注解记录操作日志
-   **参数验证**：使用 Jakarta Validation 进行参数校验
-   **异常处理**：使用`ServiceException`统一异常处理

### 添加新的 API 接口

1. 在`domain.bo`包中添加业务对象
2. 在`domain.vo`包中添加视图对象
3. 在`service`包中添加服务接口和实现
4. 在`controller`包中添加控制器方法
5. 编写相应的单元测试

### 自定义重试策略

可以通过修改`RetryUtil`类来自定义重试策略，支持：

-   指数退避重试
-   固定间隔重试
-   自定义重试条件

### 日志配置

项目使用框架统一的日志配置，可以通过修改`application.yml`中的`logging`配置来调整日志级别和格式。

## 测试

运行单元测试：

```bash
mvn test
```

运行集成测试：

```bash
mvn spring-boot:run
```

## 部署

### Docker 部署

```bash
# 构建镜像
docker build -t ruoyi-api .

# 运行容器
docker run -d -p 8080:8080 \
  -e ALIYUN_ACCESS_KEY_ID=your-access-key-id \
  -e ALIYUN_ACCESS_KEY_SECRET=your-access-key-secret \
  ruoyi-api
```

### 传统部署

```bash
# 打包
mvn clean package

# 运行
java -jar target/ruoyi-api-5.4.1.jar
```

## 许可证

本项目采用 MIT 许可证。
