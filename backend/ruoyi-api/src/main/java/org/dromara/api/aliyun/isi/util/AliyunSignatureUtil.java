package org.dromara.api.aliyun.isi.util;

import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.exception.ServiceException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 阿里云API签名工具类
 *
 * @author Lion Li
 */
@Slf4j
public class AliyunSignatureUtil {

    private static final String ALGORITHM = "HmacSHA1";
    private static final String VERSION = "2018-08-17";
    private static final String FORMAT = "JSON";
    private static final String SIGNATURE_METHOD = "HMAC-SHA1";
    private static final String SIGNATURE_VERSION = "1.0";

    /**
     * 生成阿里云API签名
     *
     * @param accessKeyId 访问密钥ID
     * @param accessKeySecret 访问密钥Secret
     * @param httpMethod HTTP方法
     * @param parameters 请求参数
     * @return 签名后的请求头
     */
    public static Map<String, String> generateSignature(String accessKeyId, String accessKeySecret, 
                                                      String httpMethod, Map<String, String> parameters) {
        try {
            // 添加公共参数
            Map<String, String> allParams = new TreeMap<>(parameters);
            allParams.put("Format", FORMAT);
            allParams.put("Version", VERSION);
            allParams.put("AccessKeyId", accessKeyId);
            allParams.put("SignatureMethod", SIGNATURE_METHOD);
            allParams.put("SignatureVersion", SIGNATURE_VERSION);
            allParams.put("SignatureNonce", UUID.randomUUID().toString());
            allParams.put("Timestamp", getISO8601Time());

            // 构建签名字符串
            String stringToSign = buildStringToSign(httpMethod, allParams);
            
            // 计算签名
            String signature = calculateSignature(stringToSign, accessKeySecret + "&");
            
            // 添加签名到参数中
            allParams.put("Signature", signature);

            // 构建请求头
            Map<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "application/x-www-form-urlencoded");
            headers.put("Authorization", "acs " + accessKeyId + ":" + signature);

            return headers;
        } catch (Exception e) {
            log.error("生成阿里云API签名失败", e);
            throw new ServiceException("生成签名失败", e);
        }
    }

    /**
     * 构建待签名字符串
     */
    private static String buildStringToSign(String httpMethod, Map<String, String> parameters) {
        StringBuilder sb = new StringBuilder();
        sb.append(httpMethod.toUpperCase()).append("&");
        sb.append(percentEncode("/")).append("&");

        // 构建参数字符串
        StringBuilder paramString = new StringBuilder();
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            if (paramString.length() > 0) {
                paramString.append("&");
            }
            paramString.append(percentEncode(entry.getKey()))
                      .append("=")
                      .append(percentEncode(entry.getValue()));
        }
        
        sb.append(percentEncode(paramString.toString()));
        return sb.toString();
    }

    /**
     * 计算HMAC-SHA1签名
     */
    private static String calculateSignature(String stringToSign, String secret) throws Exception {
        Mac mac = Mac.getInstance(ALGORITHM);
        SecretKeySpec secretKeySpec = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), ALGORITHM);
        mac.init(secretKeySpec);
        byte[] hash = mac.doFinal(stringToSign.getBytes(StandardCharsets.UTF_8));
        return java.util.Base64.getEncoder().encodeToString(hash);
    }

    /**
     * URL编码
     */
    private static String percentEncode(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.name())
                    .replace("+", "%20")
                    .replace("*", "%2A")
                    .replace("%7E", "~");
        } catch (Exception e) {
            return value;
        }
    }

    /**
     * 获取ISO8601格式的时间戳
     */
    private static String getISO8601Time() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.format(new Date());
    }
} 