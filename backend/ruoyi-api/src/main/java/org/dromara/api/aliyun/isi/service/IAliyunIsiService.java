package org.dromara.api.aliyun.isi.service;

import org.dromara.api.aliyun.isi.domain.bo.IsiTaskBo;
import org.dromara.api.aliyun.isi.domain.vo.IsiTaskVo;

/**
 * 阿里云录音文件识别服务接口
 *
 * @author Lion Li
 */
public interface IAliyunIsiService {

    /**
     * 提交录音文件识别任务
     *
     * @param taskBo 识别任务业务对象
     * @return 识别任务视图对象
     */
    IsiTaskVo submitTask(IsiTaskBo taskBo);

    /**
     * 查询录音文件识别任务状态
     *
     * @param taskId 任务ID
     * @return 识别任务视图对象
     */
    IsiTaskVo queryTask(String taskId);
} 