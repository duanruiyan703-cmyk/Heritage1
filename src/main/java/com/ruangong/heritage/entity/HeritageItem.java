package com.ruangong.heritage.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 非遗作品实体类
 * @author system
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("heritage_item")
//非遗作品实体类
public class HeritageItem {

    @TableId(type = IdType.ASSIGN_ID)
    private String id;


    private String title;


    private String category;


    private String region;

    //摘要
    private String summary;

    //描述
    private String description;

    //"状态 0草稿 1待审 2已发布 3下架"
    private Integer status;


    private String creatorId;


    //封面文件ID（通过sys_file_info表关联，business_field='cover'）
    @TableField(exist = false)
    private Long coverFileId;


    private LocalDateTime publishTime;


    private LocalDateTime createTime;


    private LocalDateTime updateTime;

    /**
     * 是否为草稿状态
     */
    public boolean isDraft() {
        return this.status != null && this.status == 0;
    }

    /**
     * 是否为待审状态
     */
    public boolean isPending() {
        return this.status != null && this.status == 1;
    }

    /**
     * 是否为已发布状态
     */
    public boolean isPublished() {
        return this.status != null && this.status == 2;
    }

    /**
     * 是否为下架状态
     */
    public boolean isOffline() {
        return this.status != null && this.status == 3;
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
                return "待审";
            case 2:
                return "已发布";
            case 3:
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

