package com.ruangong.heritage.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 课程章节实体类
 * @author system
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("course_chapter")
///课程章节实体类
public class CourseChapter {


    private Long id;


    private String courseId;


    private String title;


    private String content;


    private Integer sort;

    /**
     * 是否有内容
     */
    public boolean hasContent() {
        return content != null && !content.trim().isEmpty();
    }
}

