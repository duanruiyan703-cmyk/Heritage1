package com.ruangong.heritage.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruangong.heritage.common.Result;
import com.ruangong.heritage.dto.command.ActivitySignupCreateCommandDTO;
import com.ruangong.heritage.dto.response.ActivityDetailResponseDTO;
import com.ruangong.heritage.dto.response.ActivityResponseDTO;
import com.ruangong.heritage.dto.response.ActivitySignupResponseDTO;
import com.ruangong.heritage.service.ActivityService;
import com.ruangong.heritage.service.ActivitySignupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 活动管理控制器
 */
//活动管理
@RequestMapping("/activity")
@RestController
@Slf4j
public class ActivityController {

    @Resource
    private ActivityService activityService;

    @Resource
    private ActivitySignupService activitySignupService;

    /**
     * 分页查询活动列表
     */
    @Operation(summary = "分页查询活动列表", description = "根据条件分页查询活动列表")
    @GetMapping("/page")
    public Result<Page<ActivityResponseDTO>> getActivityPage(
            @Parameter(description = "当前页") @RequestParam(defaultValue = "1") Long current,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Long size,
            @Parameter(description = "标题关键词") @RequestParam(required = false) String title,
            @Parameter(description = "活动类型") @RequestParam(required = false) String type,
            @Parameter(description = "状态") @RequestParam(required = false) Integer status) {

        log.info("分页查询活动列表: page={}, size={}, title={}", current, size, title);

        Page<ActivityResponseDTO> response = activityService.getActivityPage(current, size, title, type, status);
        return Result.success(response);
    }

    /**
     * 根据ID获取活动详情
     */
    //获取活动详情
    @GetMapping("/{activityId}")
    public Result<ActivityDetailResponseDTO> getActivityById(
            @Parameter(description = "活动ID") @PathVariable String activityId) {
        log.info("获取活动详情请求: activityId={}", activityId);
        ActivityDetailResponseDTO response = activityService.getActivityById(activityId);
        return Result.success(response);
    }

    /**
     * 报名活动
     */
    //报名活动
    @PostMapping("/signup")
    public Result<ActivitySignupResponseDTO> signupActivity(
            @Valid @RequestBody ActivitySignupCreateCommandDTO createDTO) {
        log.info("活动报名请求: activityId={}, userId={}", createDTO.getActivityId(), createDTO.getUserId());
        ActivitySignupResponseDTO response = activitySignupService.createSignup(createDTO);
        return Result.success(response);
    }

}


