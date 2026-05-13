package com.ruangong.heritage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruangong.heritage.entity.Course;
import org.apache.ibatis.annotations.Mapper;

/**
 * 课程数据访问接口
 * @author system
 */
@Mapper
public interface CourseMapper extends BaseMapper<Course> {
}
