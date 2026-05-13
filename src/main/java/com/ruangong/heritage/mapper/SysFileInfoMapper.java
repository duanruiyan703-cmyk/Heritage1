package com.ruangong.heritage.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruangong.heritage.entity.SysFileInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SysFileInfoMapper extends BaseMapper<SysFileInfo> {
    // 使用MyBatis-Plus构造器查询，不需要自定义方法
}
