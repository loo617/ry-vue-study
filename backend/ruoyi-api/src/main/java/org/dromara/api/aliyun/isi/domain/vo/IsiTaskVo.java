package org.dromara.api.aliyun.isi.domain.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 录音文件识别任务视图对象
 *
 * @author Lion Li
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IsiTaskVo {

    /**
     * 请求ID
     */
    private String requestId;

    /**
     * 任务ID
     */
    private String taskId;

    /**
     * 任务状态
     */
    private String status;

    /**
     * 错误码
     */
    private String code;

    /**
     * 错误信息
     */
    private String message;

    /**
     * 识别结果
     */
    private IsiResultVo result;

    /**
     * 识别结果视图对象
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class IsiResultVo {
        
        /**
         * 识别文本
         */
        private String text;
        
        /**
         * 识别置信度
         */
        private Double confidence;
        
        /**
         * 时间戳信息
         */
        private String timestamps;
        
        /**
         * 说话人信息
         */
        private String speakers;
        
        /**
         * 关键词信息
         */
        private String keywords;
        
        /**
         * 任务完成时间
         */
        private String finishTime;
        
        /**
         * 任务开始时间
         */
        private String startTime;
    }
} 