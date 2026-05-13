package com.ruangong.heritage.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 课程列表响应DTO
 * @author system
 */
@Data
//课程列表响应
public class CourseResponseDTO {

    //课程ID
    private String id;

    //标题
    private String title;

    //难度等级
    private String level;
    //状态
    private Integer status;

    //状态名称
    private String statusName;

    //封面文件ID
    private Long coverFileId;

    //封面文件路径
    private String coverFilePath;

    //章节数
    private Long chapterCount;

    //创建时间
    private LocalDateTime createTime;

    //更新时间
    private LocalDateTime updateTime;
}



