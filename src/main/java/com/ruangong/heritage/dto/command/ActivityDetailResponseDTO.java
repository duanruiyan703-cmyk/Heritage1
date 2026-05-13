package com.ruangong.heritage.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 活动详情响应DTO
 * @author system
 */
@Data
@Schema(description = "活动详情响应")
public class ActivityDetailResponseDTO {

    @Schema(description = "活动ID")
    private String id;

    @Schema(description = "活动标题")
    private String title;

    @Schema(description = "活动类型")
    private String type;

    @Schema(description = "活动类型名称")
    private String typeName;

    @Schema(description = "开始时间")
    private LocalDateTime startTime;

    @Schema(description = "结束时间")
    private LocalDateTime endTime;

    @Schema(description = "活动地点")
    private String location;

    @Schema(description = "活动描述")
    private String description;

    @Schema(description = "活动状态")
    private Integer status;

    @Schema(description = "状态名称")
    private String statusName;

    @Schema(description = "封面文件ID")
    private Long coverFileId;

    @Schema(description = "封面文件路径")
    private String coverFilePath;

    @Schema(description = "最大报名人数")
    private Integer maxParticipants;

    @Schema(description = "当前报名人数")
    private Integer currentParticipants;

    @Schema(description = "报名数量")
    private Long signupCount;

    @Schema(description = "活动负责人")
    private String contactPerson;

    @Schema(description = "联系电话")
    private String contactPhone;

    @Schema(description = "活动要求/注意事项")
    private String requirements;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    /**
     * 判断是否可以报名
     */
    public boolean canSignup() {
        return status != null && status == 1; // 报名中状态
    }

    /**
     * 判断是否可以进行
     */
    public boolean isInProgress() {
        return status != null && status == 2; // 进行中状态
    }

    /**
     * 判断是否已结束
     */
    public boolean isFinished() {
        return status != null && status == 3; // 已结束状态
    }

    /**
     * 获取剩余名额
     */
    public int getRemainingSlots() {
        if (maxParticipants == null || maxParticipants <= 0) {
            return -1; // 无限制
        }
        if (currentParticipants == null) {
            return maxParticipants;
        }
        return Math.max(0, maxParticipants - currentParticipants);
    }
}
