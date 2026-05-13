package com.ruangong.heritage.dto;


import cn.hutool.core.date.DateUtil;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 文件信息响应DTO
 * @author system
 *  对应数据库中的一张表  sys_file_info
 */
@Data
//文件信息响应DTO
public class FileInfoDTO {

    private Long id;

    private String originalName;

    private String filePath;

    private Long fileSize;

    private String fileType;

    private String fileTypeDesc;

    private String businessType;

    private String businessTypeDesc;

    private String businessId;

    private String businessField;

    private Long uploadUserId;

    private Boolean isTemp;

    private Integer status;

    private String createTime;

    private String expireTime;

    private String fileExtension;

    private Boolean isExpired;

    /**
     * 设置创建时间（将LocalDateTime转换为字符串）
     */
    public void setCreateTime(LocalDateTime createTime) {
        if (createTime != null) {
            this.createTime = DateUtil.formatLocalDateTime(createTime);
        }
    }

    /**
     * 设置过期时间（将LocalDateTime转换为字符串）
     */
    public void setExpireTime(LocalDateTime expireTime) {
        if (expireTime != null) {
            this.expireTime = DateUtil.formatLocalDateTime(expireTime);
        }
    }
}
