package com.ruangong.heritage.dto.command;


import lombok.Data;

/**
 * 非遗作品创建命令DTO
 * @author system
 */
@Data
//非遗作品创建命令
public class HeritageItemCreateCommandDTO {

    //作品ID（前端UUID预生成
    private String id;


    private String title;


    private String category;


    private String region;

    //摘要
    private String summary;

    //描述
    private String description;

    //状态 0草稿 1待审 2已发布 3下架
    private Integer status;

    //封面文件ID
    private Long coverFileId;
}


