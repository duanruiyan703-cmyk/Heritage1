package com.ruangong.heritage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruangong.heritage.entity.Inheritor;
import org.apache.ibatis.annotations.Mapper;

/**
 * 传承人数据访问层
 * @author system
 */
@Mapper
public interface InheritorMapper extends BaseMapper<Inheritor> {
    // 继承BaseMapper，获得基础的CRUD操作
    // 所有复杂查询都在Service层使用Lambda构造器实现
}

