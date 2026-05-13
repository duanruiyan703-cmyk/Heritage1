package com.ruangong.heritage.service;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruangong.heritage.dto.response.ActivityDetailResponseDTO;
import com.ruangong.heritage.dto.response.ActivityResponseDTO;
import com.ruangong.heritage.entity.Activity;
import com.ruangong.heritage.entity.ActivitySignup;
import com.ruangong.heritage.entity.SysFileInfo;
import com.ruangong.heritage.exception.BusinessException;
import com.ruangong.heritage.exception.ServiceException;
import com.ruangong.heritage.mapper.ActivityMapper;
import com.ruangong.heritage.mapper.ActivitySignupMapper;
import com.ruangong.heritage.mapper.SysFileInfoMapper;
import com.ruangong.heritage.service.convert.ActivityConvert;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ActivityService {

    @Resource
    ActivitySignupMapper activitySignupMapper;
    
    @Resource
    private ActivityMapper activityMapper;
    
    @Resource
    private SysFileInfoMapper sysFileInfoMapper;

    /**
     * 分页查询活动列表
     * @param current 当前页
     * @param size 每页大小
     * @param title 标题（模糊查询）
     * @param type 活动类型
     * @param status 状态
     * @return 分页结果
     */
    public Page<ActivityResponseDTO> getActivityPage(Long current, Long size, String title, String type, Integer status) {
        try {
            Page<Activity> page = new Page<>(current, size);
            LambdaQueryWrapper<Activity> wrapper = new LambdaQueryWrapper<>();

            if (StrUtil.isNotBlank(title)) {
                wrapper.like(Activity::getTitle, title);
            }
            if (StrUtil.isNotBlank(type)) {
                wrapper.eq(Activity::getType, type);
            }
            if (status != null) {
                wrapper.eq(Activity::getStatus, status);
            }

            // 按创建时间倒序
            wrapper.orderByDesc(Activity::getCreateTime);

            Page<Activity> activityPage = activityMapper.selectPage(page, wrapper);

            // 转换为响应DTO
            List<ActivityResponseDTO> responseDTOList = activityPage.getRecords().stream()
                    .map(activity -> {
                        ActivityResponseDTO dto = ActivityConvert.entityToResponse(activity);
                        // 查询封面文件路径（只查询未删除的文件）
                        if (activity.getCoverFileId() != null) {
                            SysFileInfo coverFile = sysFileInfoMapper.selectById(activity.getCoverFileId());
                            if (coverFile != null && coverFile.getStatus() != null && coverFile.getStatus() == 1) {
                                dto.setCoverFilePath(coverFile.getFilePath());
                            }
                        }
                        return dto;
                    })
                    .collect(Collectors.toList());

            Page<ActivityResponseDTO> responsePage = new Page<>(activityPage.getCurrent(), activityPage.getSize(), activityPage.getTotal());
            responsePage.setRecords(responseDTOList);

            return responsePage;

        } catch (Exception e) {
            log.error("分页查询活动列表失败", e);
            throw new ServiceException("分页查询活动列表失败，请稍后重试");
        }
    }

    /**
     * 根据ID获取活动详情
     * @param activityId 活动ID
     * @return 活动详情
     */
    public ActivityDetailResponseDTO getActivityById(String activityId) {
        try {
            Activity activity = activityMapper.selectById(activityId);
            if (activity == null) {
                throw new BusinessException("活动不存在");
            }

            ActivityDetailResponseDTO response = ActivityConvert.entityToDetailResponse(activity);

            // 查询封面文件路径（只查询未删除的文件）
            if (activity.getCoverFileId() != null) {
                SysFileInfo coverFile = sysFileInfoMapper.selectById(activity.getCoverFileId());
                if (coverFile != null && coverFile.getStatus() != null && coverFile.getStatus() == 1) {
                    response.setCoverFilePath(coverFile.getFilePath());
                }
            }

            // 查询报名人数
            LambdaQueryWrapper<ActivitySignup> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(ActivitySignup::getActivityId, activityId); // 直接使用字符串ID
            Long signupCount = activitySignupMapper.selectCount(wrapper);
            response.setSignupCount(signupCount);

            return response;

        } catch (BusinessException e) {
            throw e;
        } catch (NumberFormatException e) {
            throw new BusinessException("活动ID格式错误");
        } catch (Exception e) {
            log.error("获取活动详情失败: activityId={}", activityId, e);
            throw new ServiceException("获取活动详情失败，请稍后重试");
        }
    }
}
