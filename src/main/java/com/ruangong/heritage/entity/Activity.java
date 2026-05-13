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
 * 活动实体类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("activity")
//活动实体类
public class Activity {

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    //活动标题
    private String title;

    //活动类型：展演/展览/培训/比赛
    private String type;

    //活动开始时间
    private LocalDateTime startTime;


    private LocalDateTime endTime;


    private String location;


    private String description;

    //活动状态：0草稿 1报名中 2进行中 3已结束
    private Integer status;

    //封面文件ID")
    @TableField("cover_file_id")
    private Long coverFileId;

    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    /**
     * 是否为草稿状态
     */
    public boolean isDraft() {
        return this.status != null && this.status == 0;
    }

    /**
     * 是否为报名中状态
     */
    public boolean isSigningUp() {
        return this.status != null && this.status == 1;
    }

    /**
     * 是否为进行中状态
     */
    public boolean isInProgress() {
        return this.status != null && this.status == 2;
    }

    /**
     * 是否为已结束状态
     */
    public boolean isFinished() {
        return this.status != null && this.status == 3;
    }

    /**
     * 是否可以报名
     */
    public boolean canSignup() {
        return isSigningUp();
    }

    /**
     * 是否可以签到
     */
    public boolean canCheckIn() {
        return isInProgress();
    }

    /**
     * 获取状态显示名称
     */
    public String getStatusDisplayName() {
        if (this.status == null) {
            return "未知";
        }
        return switch (this.status) {
            case 0 -> "草稿";
            case 1 -> "报名中";
            case 2 -> "进行中";
            case 3 -> "已结束";
            default -> "未知";
        };
    }

    /**
     * 检查活动时间是否有效
     */
    public boolean isValidTimeRange() {
        if (startTime == null || endTime == null) {
            return false;
        }
        return startTime.isBefore(endTime);
    }

    /**
     * 检查活动是否已开始
     */
    public boolean hasStarted() {
        return startTime != null && LocalDateTime.now().isAfter(startTime);
    }

    /**
     * 检查活动是否已结束
     */
    public boolean hasEnded() {
        return endTime != null && LocalDateTime.now().isAfter(endTime);
    }
}

