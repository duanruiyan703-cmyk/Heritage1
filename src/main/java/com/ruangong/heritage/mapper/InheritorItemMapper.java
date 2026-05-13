package com.ruangong.heritage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruangong.heritage.entity.InheritorItem;
import org.apache.ibatis.annotations.Mapper;


/**
 * 传承人与作品关联数据访问层
 */
@Mapper
public interface InheritorItemMapper extends BaseMapper<InheritorItem> {
    // 继承BaseMapper，获得基础的CRUD操作
    // 所有复杂查询都在Service层使用Lambda构造器实现
}

