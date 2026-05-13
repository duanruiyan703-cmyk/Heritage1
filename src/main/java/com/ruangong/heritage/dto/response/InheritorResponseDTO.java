package com.ruangong.heritage.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 传承人响应DTO
 * @author system
 */
@Data
public class InheritorResponseDTO {

    /**
     * 传承人ID
     */
    private String id;

    /**
     * 姓名
     */
    private String name;

    /**
     * 称号
     */
    private String title;

    /**
     * 地区
     */
    private String region;

    /**
     * 简介
     */
    private String bio;

    /**
     * 头像文件ID
     */
    private Long avatarFileId;

    /**
     * 头像路径
     */
    private String avatarPath;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}

