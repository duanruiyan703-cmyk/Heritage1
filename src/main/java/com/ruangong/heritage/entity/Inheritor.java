package com.ruangong.heritage.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 传承人实体类
 * @author system
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("inheritor")
//传承人实体类
public class Inheritor {

    @TableId(type = IdType.ASSIGN_ID)
    private String id;


    private String name;


    private String title;


    private String region;

    //"简介"
    private String bio;

    //头像文件ID
    @TableField("avatar_file_id")
    private Long avatarFileId;


    private LocalDateTime createTime;


    private LocalDateTime updateTime;

    /**
     * 获取业务标识（直接返回ID）
     */
    public String getBusinessId() {
        return id;
    }

    /**
     * 是否使用UUID格式的ID
     */
    public boolean isUsingUuid() {
        if (id == null || id.trim().isEmpty()) {
            return false;
        }
        // 简单的UUID格式检查：包含4个连字符的36字符字符串
        return id.length() == 36 && id.chars().filter(ch -> ch == '-').count() == 4;
    }

    /**
     * 是否使用数字格式的ID
     */
    public boolean isUsingNumericId() {
        if (id == null || id.trim().isEmpty()) {
            return false;
        }
        try {
            Long.parseLong(id);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * 获取显示名称（姓名 + 称号）
     */
    public String getDisplayName() {
        if (title != null && !title.trim().isEmpty()) {
            return name + "（" + title + "）";
        }
        return name;
    }

    /**
     * 获取简短简介（最多50字）
     */
    public String getShortBio() {
        if (bio == null || bio.trim().isEmpty()) {
            return "";
        }
        if (bio.length() <= 50) {
            return bio;
        }
        return bio.substring(0, 50) + "...";
    }
}

