package com.ruangong.heritage.service.convert;


import com.ruangong.heritage.dto.response.ActivityDetailResponseDTO;
import com.ruangong.heritage.dto.response.ActivityResponseDTO;
import com.ruangong.heritage.entity.Activity;

public class ActivityConvert {

    /**
     * 实体转列表响应DTO
     */
    public static ActivityResponseDTO entityToResponse(Activity activity) {
        ActivityResponseDTO response = new ActivityResponseDTO();
        response.setId(activity.getId());
        response.setTitle(activity.getTitle());
        response.setType(activity.getType());
        response.setStartTime(activity.getStartTime());
        response.setEndTime(activity.getEndTime());
        response.setLocation(activity.getLocation());
        response.setStatus(activity.getStatus());
        response.setStatusName(activity.getStatusDisplayName());
        response.setCoverFileId(activity.getCoverFileId());
        response.setCreateTime(activity.getCreateTime());
        response.setUpdateTime(activity.getUpdateTime());
        return response;
    }

    /**
     * 实体转详情响应DTO
     */
    public static ActivityDetailResponseDTO entityToDetailResponse(Activity activity) {
        ActivityDetailResponseDTO response = new ActivityDetailResponseDTO();
        response.setId(activity.getId());
        response.setTitle(activity.getTitle());
        response.setType(activity.getType());
        response.setStartTime(activity.getStartTime());
        response.setEndTime(activity.getEndTime());
        response.setLocation(activity.getLocation());
        response.setDescription(activity.getDescription());
        response.setStatus(activity.getStatus());
        response.setStatusName(activity.getStatusDisplayName());
        response.setCoverFileId(activity.getCoverFileId());
        response.setCreateTime(activity.getCreateTime());
        response.setUpdateTime(activity.getUpdateTime());
        return response;
    }
}
