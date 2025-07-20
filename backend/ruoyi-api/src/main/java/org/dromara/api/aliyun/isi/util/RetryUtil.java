package org.dromara.api.aliyun.isi.util;

import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.exception.ServiceException;
import org.springframework.web.client.RestClientException;

import java.util.function.Supplier;

/**
 * 重试工具类
 *
 * @author Lion Li
 */
@Slf4j
public class RetryUtil {

    /**
     * 执行带重试的操作
     *
     * @param supplier 操作提供者
     * @param maxRetries 最大重试次数
     * @param retryInterval 重试间隔（毫秒）
     * @param <T> 返回类型
     * @return 操作结果
     */
    public static <T> T executeWithRetry(Supplier<T> supplier, int maxRetries, long retryInterval) {
        int attempts = 0;
        Exception lastException = null;

        while (attempts <= maxRetries) {
            try {
                return supplier.get();
            } catch (Exception e) {
                lastException = e;
                attempts++;
                
                if (attempts <= maxRetries) {
                    log.warn("操作失败，第{}次重试，错误: {}", attempts, e.getMessage());
                    try {
                        Thread.sleep(retryInterval * attempts); // 指数退避
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        throw new ServiceException("重试被中断", ie);
                    }
                }
            }
        }

        log.error("操作失败，已重试{}次", maxRetries, lastException);
        throw new ServiceException("操作失败，已重试" + maxRetries + "次", lastException);
    }

    /**
     * 执行带重试的操作（使用默认重试间隔）
     *
     * @param supplier 操作提供者
     * @param maxRetries 最大重试次数
     * @param <T> 返回类型
     * @return 操作结果
     */
    public static <T> T executeWithRetry(Supplier<T> supplier, int maxRetries) {
        return executeWithRetry(supplier, maxRetries, 1000L);
    }
} 