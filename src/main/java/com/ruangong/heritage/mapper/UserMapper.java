package com.ruangong.heritage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruangong.heritage.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    // 继承BaseMapper，获得基础的CRUD操作
    // 所有复杂查询都在Service层使用Lambda构造器实现
}
