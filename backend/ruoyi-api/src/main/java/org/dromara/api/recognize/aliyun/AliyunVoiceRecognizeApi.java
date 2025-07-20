package org.dromara.api.recognize.aliyun;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.dromara.api.recognize.aliyun.config.AliyunIsiProperties;
import org.dromara.api.recognize.aliyun.domain.bo.IsiTaskBo;
import org.dromara.api.recognize.aliyun.domain.vo.IsiTaskVo;
import org.dromara.api.recognize.aliyun.util.AliyunSignatureUtil;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class AliyunVoiceRecognizeApi {

    @Resource
    private AliyunIsiProperties aliyunIsiProperties;

    @Resource(name = "aliyunRestClient")
    private RestClient restClient;



    public IsiTaskVo submitTask(IsiTaskBo taskBo) {
        log.info("提交录音文件识别任务: {}", taskBo.getFileLink());
        Map<String, String> parameters = buildSubmitTaskParameters(taskBo);
        Map<String, String> headers = AliyunSignatureUtil.generateSignature(
                aliyunIsiProperties.getAccessKeyId(),
                aliyunIsiProperties.getAccessKeySecret(),
                "POST",
                parameters
        );

        HttpHeaders httpHeaders = new HttpHeaders();
        headers.forEach(httpHeaders::add);
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String requestBody = buildRequestBody(parameters);
        HttpEntity<String> entity = new HttpEntity<>(requestBody, httpHeaders);

        return restClient.method(HttpMethod.POST)
                .uri(aliyunIsiProperties.getEndpoint())
                .body(entity)
                .retrieve()
                .body(IsiTaskVo.class);
    }

    public IsiTaskVo queryTask(String taskId) {
        log.info("查询录音文件识别任务状态: {}", taskId);
        Map<String, String> parameters = buildQueryTaskParameters(taskId);
        Map<String, String> headers = AliyunSignatureUtil.generateSignature(
                aliyunIsiProperties.getAccessKeyId(),
                aliyunIsiProperties.getAccessKeySecret(),
                "GET",
                parameters
        );

        HttpHeaders httpHeaders = new HttpHeaders();
        headers.forEach(httpHeaders::add);
        HttpEntity<String> entity = new HttpEntity<>(httpHeaders);

        return restClient.method(HttpMethod.GET)
                .uri(aliyunIsiProperties.getEndpoint() + "?" + buildQueryString(parameters))
                .body(entity)
                .retrieve()
                .body(IsiTaskVo.class);
    }


    /**
     * 构建提交任务参数
     */
    private Map<String, String> buildSubmitTaskParameters(IsiTaskBo taskBo) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("Action", "SubmitTask");
        parameters.put("FileLink", taskBo.getFileLink());
        parameters.put("Format", taskBo.getFormat());
        parameters.put("Duration", String.valueOf(taskBo.getDuration()));

        if (taskBo.getTaskName() != null) {
            parameters.put("TaskName", taskBo.getTaskName());
        }
        if (taskBo.getTaskDescription() != null) {
            parameters.put("TaskDescription", taskBo.getTaskDescription());
        }
        if (taskBo.getCallbackUrl() != null) {
            parameters.put("CallbackUrl", taskBo.getCallbackUrl());
        }
        if (taskBo.getCallbackParams() != null) {
            parameters.put("CallbackParams", taskBo.getCallbackParams());
        }
        if (taskBo.getEnablePunctuation() != null) {
            parameters.put("EnablePunctuation", String.valueOf(taskBo.getEnablePunctuation()));
        }
        if (taskBo.getEnableTimestamp() != null) {
            parameters.put("EnableTimestamp", String.valueOf(taskBo.getEnableTimestamp()));
        }
        if (taskBo.getEnableSpeakerDiarization() != null) {
            parameters.put("EnableSpeakerDiarization", String.valueOf(taskBo.getEnableSpeakerDiarization()));
        }
        if (taskBo.getEnableKeywordExtraction() != null) {
            parameters.put("EnableKeywordExtraction", String.valueOf(taskBo.getEnableKeywordExtraction()));
        }
        if (taskBo.getLanguage() != null) {
            parameters.put("Language", taskBo.getLanguage());
        }
        if (taskBo.getSampleRate() != null) {
            parameters.put("SampleRate", String.valueOf(taskBo.getSampleRate()));
        }
        if (taskBo.getChannelNum() != null) {
            parameters.put("ChannelNum", String.valueOf(taskBo.getChannelNum()));
        }

        return parameters;
    }

    /**
     * 构建查询任务参数
     */
    private Map<String, String> buildQueryTaskParameters(String taskId) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("Action", "GetTaskInfo");
        parameters.put("TaskId", taskId);
        return parameters;
    }

    /**
     * 构建请求体
     */
    private String buildRequestBody(Map<String, String> parameters) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            if (sb.length() > 0) {
                sb.append("&");
            }
            sb.append(entry.getKey()).append("=").append(entry.getValue());
        }
        return sb.toString();
    }

    /**
     * 构建查询字符串
     */
    private String buildQueryString(Map<String, String> parameters) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            if (sb.length() > 0) {
                sb.append("&");
            }
            sb.append(entry.getKey()).append("=").append(entry.getValue());
        }
        return sb.toString();
    }

}
