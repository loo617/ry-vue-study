package org.dromara.api.aliyun.isi.domain.bo;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 录音文件识别任务业务对象
 *
 * @author Lion Li
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IsiTaskBo {

    /**
     * 录音文件URL
     */
    @NotBlank(message = "录音文件URL不能为空")
    private String fileLink;

    /**
     * 录音文件格式
     */
    @NotBlank(message = "录音文件格式不能为空")
    private String format;

    /**
     * 录音文件时长（秒）
     */
    @NotNull(message = "录音文件时长不能为空")
    private Integer duration;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 任务描述
     */
    private String taskDescription;

    /**
     * 回调URL
     */
    private String callbackUrl;

    /**
     * 回调参数
     */
    private String callbackParams;

    /**
     * 是否启用标点符号
     */
    private Boolean enablePunctuation = true;

    /**
     * 是否启用时间戳
     */
    private Boolean enableTimestamp = false;

    /**
     * 是否启用说话人分离
     */
    private Boolean enableSpeakerDiarization = false;

    /**
     * 是否启用关键词识别
     */
    private Boolean enableKeywordExtraction = false;

    /**
     * 语言类型
     */
    private String language = "zh";

    /**
     * 音频采样率
     */
    private Integer sampleRate = 16000;

    /**
     * 音频声道数
     */
    private Integer channelNum = 1;
} 