package com.ruangong.heritage.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruangong.heritage.dto.response.CourseChapterResponseDTO;
import com.ruangong.heritage.dto.response.CourseDetailResponseDTO;
import com.ruangong.heritage.dto.response.CourseResponseDTO;
import com.ruangong.heritage.entity.Course;
import com.ruangong.heritage.entity.CourseChapter;
import com.ruangong.heritage.entity.SysFileInfo;
import com.ruangong.heritage.exception.BusinessException;
import com.ruangong.heritage.exception.ServiceException;
import com.ruangong.heritage.mapper.CourseChapterMapper;
import com.ruangong.heritage.mapper.CourseMapper;
import com.ruangong.heritage.mapper.SysFileInfoMapper;
import com.ruangong.heritage.service.convert.CourseChapterConvert;
import com.ruangong.heritage.service.convert.CourseConvert;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 课程业务逻辑层
 * @author system
 */
@Slf4j
@Service
public class CourseService {

    @Resource
    private CourseMapper courseMapper;

    @Resource
    private CourseChapterMapper courseChapterMapper;

    @Resource
    private SysFileInfoMapper sysFileInfoMapper;

    @Resource
    private FileService fileService;


    /**
     * 分页查询课程列表
     * @param current 当前页
     * @param size 每页大小
     * @param title 标题（模糊查询）
     * @param level 难度等级
     * @param status 状态
     * @return 分页结果
     */
    public Page<CourseResponseDTO> getCoursePage(Long current, Long size, String title, String level, Integer status) {
        try {
            Page<Course> page = new Page<>(current, size);
            LambdaQueryWrapper<Course> wrapper = new LambdaQueryWrapper<>();

            if (StrUtil.isNotBlank(title)) {
                wrapper.like(Course::getTitle, title);
            }
            if (StrUtil.isNotBlank(level)) {
                wrapper.eq(Course::getLevel, level);
            }
            if (status != null) {
                wrapper.eq(Course::getStatus, status);
            }

            // 按创建时间倒序
            wrapper.orderByDesc(Course::getCreateTime);

            Page<Course> coursePage = courseMapper.selectPage(page, wrapper);

            // 转换为响应DTO
            List<CourseResponseDTO> responseDTOList = coursePage.getRecords().stream()
                    .map(course -> {
                        CourseResponseDTO dto = CourseConvert.entityToResponse(course);
                        // 查询封面文件路径
                        if (course.getCoverFileId() != null) {
                            SysFileInfo coverFile = sysFileInfoMapper.selectById(course.getCoverFileId());
                            if (coverFile != null) {
                                dto.setCoverFilePath(coverFile.getFilePath());
                            }
                        }
                        // 查询章节数
                        LambdaQueryWrapper<CourseChapter> chapterWrapper = new LambdaQueryWrapper<>();
                        chapterWrapper.eq(CourseChapter::getCourseId, course.getId());
                        Long chapterCount = courseChapterMapper.selectCount(chapterWrapper);
                        dto.setChapterCount(chapterCount);
                        return dto;
                    })
                    .collect(Collectors.toList());

            Page<CourseResponseDTO> responsePage = new Page<>(coursePage.getCurrent(), coursePage.getSize(), coursePage.getTotal());
            responsePage.setRecords(responseDTOList);

            return responsePage;

        } catch (Exception e) {
            log.error("分页查询课程列表失败", e);
            throw new ServiceException("分页查询课程列表失败，请稍后重试");
        }
    }

    /**
     * 根据ID获取课程详情
     * @param courseId 课程ID
     * @return 课程详情
     */
    public CourseDetailResponseDTO getCourseById(String courseId) {
        try {
            Course course = courseMapper.selectById(courseId);
            if (course == null) {
                throw new BusinessException("课程不存在");
            }

            CourseDetailResponseDTO response = CourseConvert.entityToDetailResponse(course);

            // 查询封面文件路径
            if (course.getCoverFileId() != null) {
                SysFileInfo coverFile = sysFileInfoMapper.selectById(course.getCoverFileId());
                if (coverFile != null) {
                    response.setCoverFilePath(coverFile.getFilePath());
                }
            }

            // 查询章节列表
            LambdaQueryWrapper<CourseChapter> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(CourseChapter::getCourseId, courseId)
                    .orderByAsc(CourseChapter::getSort);
            List<CourseChapter> chapters = courseChapterMapper.selectList(wrapper);

            List<CourseChapterResponseDTO> chapterDTOs = chapters.stream()
                    .map(chapter -> {
                        CourseChapterResponseDTO dto = CourseChapterConvert.entityToResponse(chapter);
                        // 通过文件系统关联查询视频文件
                        dto.setVideoFiles(fileService.getFilesByBusinessField(
                                "COURSE_CHAPTER",
                                String.valueOf(chapter.getId()),
                                "video"
                        ));
                        return dto;
                    })
                    .collect(Collectors.toList());

            response.setChapters(chapterDTOs);

            return response;

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("获取课程详情失败: courseId={}", courseId, e);
            throw new ServiceException("获取课程详情失败，请稍后重试");
        }
    }

}

