package org.dromara.api.aliyun.isi.service;

import org.dromara.api.aliyun.isi.config.AliyunIsiProperties;
import org.dromara.api.aliyun.isi.domain.bo.IsiTaskBo;
import org.dromara.api.aliyun.isi.domain.vo.IsiTaskVo;
import org.dromara.api.aliyun.isi.service.impl.AliyunIsiServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClient;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * 阿里云录音文件识别服务测试
 *
 * @author Lion Li
 */
@ExtendWith(MockitoExtension.class)
class AliyunIsiServiceTest {

    @Mock
    private AliyunIsiProperties aliyunIsiProperties;

    @Mock
    private RestClient restClient;

    @InjectMocks
    private AliyunIsiServiceImpl aliyunIsiService;

    @BeforeEach
    void setUp() {
        when(aliyunIsiProperties.getAccessKeyId()).thenReturn("test-access-key-id");
        when(aliyunIsiProperties.getAccessKeySecret()).thenReturn("test-access-key-secret");
        when(aliyunIsiProperties.getEndpoint()).thenReturn("https://isi.cn-shanghai.aliyuncs.com");
        when(aliyunIsiProperties.getMaxRetries()).thenReturn(3);
        when(aliyunIsiProperties.getRetryInterval()).thenReturn(1000);
    }

    @Test
    void testSubmitTask() {
        // 准备测试数据
        IsiTaskBo taskBo = IsiTaskBo.builder()
                .fileLink("https://example.com/audio.wav")
                .format("wav")
                .duration(60)
                .taskName("测试任务")
                .enablePunctuation(true)
                .language("zh")
                .build();

        IsiTaskVo expectedResponse = new IsiTaskVo();
        expectedResponse.setTaskId("test-task-id");
        expectedResponse.setStatus("SUBMITTED");

        // 模拟RestClient响应
        when(restClient.method(any()))
                .thenReturn(RestClient.RequestHeadersUriSpec.create(restClient, null));
        when(restClient.method(any()).uri(anyString()))
                .thenReturn(RestClient.RequestBodyUriSpec.create(restClient, null));
        when(restClient.method(any()).uri(anyString()).body(any()))
                .thenReturn(RestClient.ResponseSpec.create(restClient, null));
        when(restClient.method(any()).uri(anyString()).body(any()).retrieve())
                .thenReturn(RestClient.ResponseSpec.create(restClient, null));
        when(restClient.method(any()).uri(anyString()).body(any()).retrieve().body(IsiTaskVo.class))
                .thenReturn(expectedResponse);

        // 执行测试
        IsiTaskVo response = aliyunIsiService.submitTask(taskBo);

        // 验证结果
        assertNotNull(response);
        assertEquals("test-task-id", response.getTaskId());
        assertEquals("SUBMITTED", response.getStatus());
    }

    @Test
    void testQueryTask() {
        // 准备测试数据
        String taskId = "test-task-id";

        IsiTaskVo expectedResponse = new IsiTaskVo();
        expectedResponse.setTaskId(taskId);
        expectedResponse.setStatus("SUCCEEDED");

        // 模拟RestClient响应
        when(restClient.method(any()))
                .thenReturn(RestClient.RequestHeadersUriSpec.create(restClient, null));
        when(restClient.method(any()).uri(anyString()))
                .thenReturn(RestClient.RequestBodyUriSpec.create(restClient, null));
        when(restClient.method(any()).uri(anyString()).body(any()))
                .thenReturn(RestClient.ResponseSpec.create(restClient, null));
        when(restClient.method(any()).uri(anyString()).body(any()).retrieve())
                .thenReturn(RestClient.ResponseSpec.create(restClient, null));
        when(restClient.method(any()).uri(anyString()).body(any()).retrieve().body(IsiTaskVo.class))
                .thenReturn(expectedResponse);

        // 执行测试
        IsiTaskVo response = aliyunIsiService.queryTask(taskId);

        // 验证结果
        assertNotNull(response);
        assertEquals(taskId, response.getTaskId());
        assertEquals("SUCCEEDED", response.getStatus());
    }
} 