package com.ruangong.heritage.dto.response;


import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 非遗作品详情响应DTO
 * @author system
 */
@Data
public class HeritageItemDetailResponseDTO {

    private String id;

    private String title;

    private String category;

    private String region;

    //摘要
    private String summary;

    //描述
    private String description;

    //状态码
    private Integer status;

    //状态名称
    private String statusName;

    //创建人ID
    private String creatorId;

    //创建人姓名
    private String creatorName;

    //发布时间
    private LocalDateTime publishTime;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    //封面文件ID
    private Long coverFileId;

    //封面图片路径
    private String coverImage;

    //关联媒体文件列表
    private List<HeritageItemMediaResponseDTO> mediaList;

    //关联传承人列表
    private List<HeritageItemInheritorResponseDTO> inheritorList;
}



