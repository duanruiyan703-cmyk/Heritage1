package com.ruangong.heritage.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruangong.heritage.common.Result;
import com.ruangong.heritage.dto.response.InheritorDetailResponseDTO;
import com.ruangong.heritage.dto.response.InheritorResponseDTO;
import com.ruangong.heritage.service.InheritorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 传承人管理控制器
 * @author system
 */
//传承人管理
@RequestMapping("/inheritor")
@RestController
@Slf4j
public class InheritorController {

    @Resource
    private InheritorService inheritorService;



    /**
     * 分页查询传承人列表
     */
    @Operation(summary = "分页查询传承人列表", description = "根据条件分页查询传承人列表")
    @GetMapping("/page")
    public Result<Page<InheritorResponseDTO>> getInheritorPage(
            @Parameter(description = "当前页") @RequestParam(defaultValue = "1") Long current,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Long size,
            @Parameter(description = "姓名关键词") @RequestParam(required = false) String name,
            @Parameter(description = "称号") @RequestParam(required = false) String title,
            @Parameter(description = "地区") @RequestParam(required = false) String region) {

        log.info("分页查询传承人列表: page={}, size={}, name={}", current, size, name);

        Page<InheritorResponseDTO> response = inheritorService.getInheritorPage(current, size, name, title, region);
        return Result.success(response);
    }

    /**
     * 根据ID获取传承人详情
     */
    //获取传承人详情，根据传承人ID获取详细信息
    @GetMapping("/{inheritorId}")
    public Result<InheritorDetailResponseDTO> getInheritorById(
            @Parameter(description = "传承人ID") @PathVariable String inheritorId) {
        log.info("获取传承人详情请求: inheritorId={}", inheritorId);
        InheritorDetailResponseDTO response = inheritorService.getInheritorById(inheritorId);
        return Result.success(response);
    }



}

