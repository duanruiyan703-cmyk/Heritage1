package com.ruangong.heritage.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 活动报名响应DTO
 */
@Data
//活动报名响应
public class ActivitySignupResponseDTO {

    //报名ID
    private Long id;

    //活动ID
    private String activityId;

    //活动标题
    private String activityTitle;

    //用户ID
    private Long userId;

    //用户名
    private String username;

    //状态 0待审 1通过 2拒绝 3已签到
    private Integer status;

    //状态名称
    private String statusName;


    private LocalDateTime createTime;
}


