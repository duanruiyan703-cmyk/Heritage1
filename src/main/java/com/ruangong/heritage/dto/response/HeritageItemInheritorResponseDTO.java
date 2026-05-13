package com.ruangong.heritage.dto.response;

import lombok.Data;

/**
 * 非遗作品传承人响应DTO
 * @author system
 */
@Data
public class HeritageItemInheritorResponseDTO {

    //传承人ID
    private String id;

    //姓名
    private String name;

    //称号
    private String title;

    //地区
    private String region;

    //头像路径
    private String avatarPath;
}
