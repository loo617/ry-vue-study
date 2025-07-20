package org.dromara.api.aliyun.isi.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.api.aliyun.isi.domain.bo.IsiTaskBo;
import org.dromara.api.aliyun.isi.domain.vo.IsiTaskVo;
import org.dromara.api.aliyun.isi.service.IAliyunIsiService;
import org.dromara.common.core.domain.R;
import org.dromara.common.log.annotation.Log;
import org.dromara.common.log.enums.BusinessType;
import org.dromara.common.web.core.BaseController;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

/**
 * 录音文件识别控制器
 *
 * @author Lion Li
 */
@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/isi")
public class IsiController extends BaseController {

    private final IAliyunIsiService aliyunIsiService;

    /**
     * 提交录音文件识别任务
     */
    @SaCheckPermission("api:isi:submit")
    @Log(title = "录音文件识别", businessType = BusinessType.INSERT)
    @PostMapping("/submit")
    public R<IsiTaskVo> submitTask(@Valid @RequestBody IsiTaskBo taskBo) {
        log.info("接收到录音文件识别任务请求: {}", taskBo.getFileLink());
        
        try {
            IsiTaskVo response = aliyunIsiService.submitTask(taskBo);
            log.info("录音文件识别任务提交成功，任务ID: {}", response.getTaskId());
            return R.ok(response);
        } catch (Exception e) {
            log.error("录音文件识别任务提交失败", e);
            return R.fail("录音文件识别任务提交失败: " + e.getMessage());
        }
    }

    /**
     * 查询录音文件识别任务状态
     */
    @SaCheckPermission("api:isi:query")
    @GetMapping("/query/{taskId}")
    public R<IsiTaskVo> queryTask(@PathVariable @NotBlank String taskId) {
        log.info("查询录音文件识别任务状态: {}", taskId);
        
        try {
            IsiTaskVo response = aliyunIsiService.queryTask(taskId);
            log.info("录音文件识别任务状态查询成功，任务ID: {}", taskId);
            return R.ok(response);
        } catch (Exception e) {
            log.error("录音文件识别任务状态查询失败，任务ID: {}", taskId, e);
            return R.fail("录音文件识别任务状态查询失败: " + e.getMessage());
        }
    }

    /**
     * 健康检查接口
     */
    @GetMapping("/health")
    public R<String> health() {
        return R.ok("录音文件识别服务运行正常");
    }
} 