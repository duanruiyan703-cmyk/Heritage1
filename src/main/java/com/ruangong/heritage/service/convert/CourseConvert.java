package com.ruangong.heritage.service.convert;


import com.ruangong.heritage.dto.response.CourseDetailResponseDTO;
import com.ruangong.heritage.dto.response.CourseResponseDTO;
import com.ruangong.heritage.entity.Course;

public class CourseConvert {
    /**
     * 实体转列表响应DTO
     */
    public static CourseResponseDTO entityToResponse(Course course) {
        CourseResponseDTO response = new CourseResponseDTO();
        response.setId(course.getId());
        response.setTitle(course.getTitle());
        response.setLevel(course.getLevel());
        response.setStatus(course.getStatus());
        response.setStatusName(course.getStatusDisplayName());
        response.setCoverFileId(course.getCoverFileId());
        response.setCreateTime(course.getCreateTime());
        response.setUpdateTime(course.getUpdateTime());
        return response;
    }


    /**
     * 实体转详情响应DTO
     */
    public static CourseDetailResponseDTO entityToDetailResponse(Course course) {
        CourseDetailResponseDTO response = new CourseDetailResponseDTO();
        response.setId(course.getId());
        response.setTitle(course.getTitle());
        response.setLevel(course.getLevel());
        response.setDescription(course.getDescription());
        response.setStatus(course.getStatus());
        response.setStatusName(course.getStatusDisplayName());
        response.setCoverFileId(course.getCoverFileId());
        response.setCreateTime(course.getCreateTime());
        response.setUpdateTime(course.getUpdateTime());
        return response;
    }
}

