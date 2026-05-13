package com.ruangong.heritage.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruangong.heritage.common.Result;
import com.ruangong.heritage.dto.response.CourseDetailResponseDTO;
import com.ruangong.heritage.dto.response.CourseResponseDTO;
import com.ruangong.heritage.service.CourseService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 课程管理控制器
 */
//课程管理
@RequestMapping("/course")
@RestController
@Slf4j
public class CourseController {

    @Resource
    private CourseService courseService;

    /**
     * 分页查询课程列表
     */
    //分页查询课程列表
    @GetMapping("/page")
    public Result<Page<CourseResponseDTO>> getCoursePage(
            @Parameter(description = "当前页") @RequestParam(defaultValue = "1") Long current,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Long size,
            @Parameter(description = "标题关键词") @RequestParam(required = false) String title,
            @Parameter(description = "难度等级") @RequestParam(required = false) String level,
            @Parameter(description = "状态") @RequestParam(required = false) Integer status) {

        log.info("分页查询课程列表: page={}, size={}, title={}", current, size, title);

        Page<CourseResponseDTO> response = courseService.getCoursePage(current, size, title, level, status);
        return Result.success(response);
    }

    /**
     * 根据ID获取课程详情
     */

    @GetMapping("/{courseId}")
    public Result<CourseDetailResponseDTO> getCourseById(
            @Parameter(description = "课程ID") @PathVariable String courseId) {
        log.info("获取课程详情请求: courseId={}", courseId);
        CourseDetailResponseDTO response = courseService.getCourseById(courseId);
        return Result.success(response);
    }
}


