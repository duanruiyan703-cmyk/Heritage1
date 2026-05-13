package com.ruangong.heritage.service;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruangong.heritage.dto.command.ActivitySignupCreateCommandDTO;
import com.ruangong.heritage.dto.response.ActivitySignupResponseDTO;
import com.ruangong.heritage.entity.Activity;
import com.ruangong.heritage.entity.ActivitySignup;
import com.ruangong.heritage.entity.User;
import com.ruangong.heritage.exception.BusinessException;
import com.ruangong.heritage.exception.ServiceException;
import com.ruangong.heritage.mapper.ActivityMapper;
import com.ruangong.heritage.mapper.ActivitySignupMapper;
import com.ruangong.heritage.mapper.UserMapper;
import com.ruangong.heritage.service.convert.ActivitySignupConvert;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class ActivitySignupService {


    @Resource
    private ActivitySignupMapper activitySignupMapper;

    @Resource
    private ActivityMapper activityMapper;

    @Resource
    private UserMapper userMapper;

    /**
     * 创建报名
     * @param createDTO 创建命令
     * @return 报名详情
     */
    @Transactional(rollbackFor = Exception.class)
    public ActivitySignupResponseDTO createSignup(ActivitySignupCreateCommandDTO createDTO) {
        try {
            // 验证活动是否存在
            Activity activity = activityMapper.selectById(createDTO.getActivityId());
            if (activity == null) {
                throw new BusinessException("活动不存在");
            }

            // 验证活动状态是否可以报名
            if (!activity.canSignup()) {
                String activityStatusName = activity.getStatusDisplayName();
                if (activity.isDraft()) {
                    throw new BusinessException("活动还在筹备中(状态: " + activityStatusName + ")，暂时不接受报名");
                } else if (activity.isInProgress()) {
                    throw new BusinessException("活动已经开始(状态: " + activityStatusName + ")，报名时间已过");
                } else if (activity.isFinished()) {
                    throw new BusinessException("活动已经结束(状态: " + activityStatusName + ")，无法报名");
                } else {
                    throw new BusinessException("活动当前状态(" + activityStatusName + ")不接受报名，只有报名中的活动才能报名");
                }
            }

            // 验证用户是否存在
            User user = userMapper.selectById(createDTO.getUserId());
            if (user == null) {
                throw new BusinessException("用户不存在");
            }

            // 检查是否已经报名
            LambdaQueryWrapper<ActivitySignup> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(ActivitySignup::getActivityId, createDTO.getActivityId())
                    .eq(ActivitySignup::getUserId, createDTO.getUserId());
            ActivitySignup existingSignup = activitySignupMapper.selectOne(wrapper);
            if (existingSignup != null) {
                throw new BusinessException("您已经报名该活动");
            }

            // 创建报名记录
            ActivitySignup signup = ActivitySignupConvert.createCommandToEntity(createDTO);
            activitySignupMapper.insert(signup);

            log.info("活动报名成功: activityId={}, userId={}", createDTO.getActivityId(), createDTO.getUserId());

            // 查询并返回详情
            return getSignupById(signup.getId());

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("活动报名失败", e);
            throw new ServiceException("活动报名失败，请稍后重试");
        }
    }

    /**
     * 根据ID获取报名详情
     * @param signupId 报名ID
     * @return 报名详情
     */
    public ActivitySignupResponseDTO getSignupById(Long signupId) {
        try {
            ActivitySignup signup = activitySignupMapper.selectById(signupId);
            if (signup == null) {
                throw new BusinessException("报名记录不存在");
            }

            ActivitySignupResponseDTO response = ActivitySignupConvert.entityToResponse(signup);

            // 查询活动标题
            Activity activity = activityMapper.selectById(signup.getActivityId());
            if (activity != null) {
                response.setActivityTitle(activity.getTitle());
            }

            // 查询用户名
            User user = userMapper.selectById(signup.getUserId());
            if (user != null) {
                response.setUsername(user.getUsername());
            }

            return response;

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("获取报名详情失败: signupId={}", signupId, e);
            throw new ServiceException("获取报名详情失败，请稍后重试");
        }
    }
}

