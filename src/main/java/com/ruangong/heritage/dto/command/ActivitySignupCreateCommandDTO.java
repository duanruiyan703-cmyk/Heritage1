package com.ruangong.heritage.dto.command;

import lombok.Data;

/**
 * 活动报名创建命令DTO
 */
@Data
//活动报名创建命令"
public class ActivitySignupCreateCommandDTO {


    private String activityId;
    private Long userId;
}


