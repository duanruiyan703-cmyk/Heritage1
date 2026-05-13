package com.ruangong.heritage.dto.response;


import lombok.Data;

/**
 * 非遗作品媒体响应DTO
 * @author system
 */
@Data
//非遗作品媒体响应
public class HeritageItemMediaResponseDTO {

    //媒体关联ID
    private Long id;

    //文件ID
    private Long fileId;

    //文件路径
    private String filePath;

    //原始文件名
    private String originalName;

    //文件大小
    private Long fileSize;

    //媒体类型
    private String type;

    //排序
    private Integer sort;
}



