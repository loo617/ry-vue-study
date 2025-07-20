package org.dromara.api.recognize.aliyun.config;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * 阿里云录音文件识别配置属性
 *
 * @author Lion Li
 */
@Data
@Component
public class AliyunIsiProperties {

    /**
     * 阿里云访问密钥ID
     */
    private String accessKeyId;

    /**
     * 阿里云访问密钥Secret
     */
    private String accessKeySecret;

    /**
     * 阿里云区域ID
     */
    private String regionId = "cn-shanghai";

    /**
     * 录音文件识别服务端点
     */
    private String endpoint = "https://isi.cn-shanghai.aliyuncs.com";

    /**
     * 连接超时时间（毫秒）
     */
    private int connectTimeout = 10000;

    /**
     * 读取超时时间（毫秒）
     */
    private int readTimeout = 30000;

    /**
     * 最大重试次数
     */
    private int maxRetries = 3;

    /**
     * 重试间隔时间（毫秒）
     */
    private int retryInterval = 1000;
} 