package com.ruangong.heritage.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 课程实体类
 * @author system
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("course")
//课程实体类
public class Course {


    private String id;


    private String title;


    private String level;


    private String description;

    //状态 0草稿 1已发布 2下架"
    private Integer status;

    //封面文件ID
    @TableField("cover_file_id")
    private Long coverFileId;

    //创建时间
    private LocalDateTime createTime;

    //更新时间
    private LocalDateTime updateTime;

    /**
     * 是否为草稿状态
     */
    public boolean isDraft() {
        return this.status != null && this.status == 0;
    }

    /**
     * 是否为已发布状态
     */
    public boolean isPublished() {
        return this.status != null && this.status == 1;
    }

    /**
     * 是否为下架状态
     */
    public boolean isOffline() {
        return this.status != null && this.status == 2;
    }

    /**
     * 获取状态显示名称
     */
    public String getStatusDisplayName() {
        if (status == null) {
            return "未知";
        }
        switch (status) {
            case 0:
                return "草稿";
            case 1:
                return "已发布";
            case 2:
                return "下架";
            default:
                return "未知";
        }
    }

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
}


