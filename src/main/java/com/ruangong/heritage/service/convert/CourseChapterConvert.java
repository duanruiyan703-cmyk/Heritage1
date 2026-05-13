package com.ruangong.heritage.service.convert;


import com.ruangong.heritage.dto.response.CourseChapterResponseDTO;
import com.ruangong.heritage.entity.CourseChapter;

public class CourseChapterConvert {


    /**
     * 实体转响应DTO
     * 注意：视频文件信息通过Service层查询文件系统获取
     */
    public static CourseChapterResponseDTO entityToResponse(CourseChapter chapter) {
        CourseChapterResponseDTO response = new CourseChapterResponseDTO();
        response.setId(chapter.getId());
        response.setCourseId(chapter.getCourseId());
        response.setTitle(chapter.getTitle());
        response.setContent(chapter.getContent());
        response.setSort(chapter.getSort());
        // videoFiles字段由Service层填充
        return response;
    }
}
