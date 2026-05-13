package com.ruangong.heritage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruangong.heritage.dto.command.HeritageItemListQueryDTO;
import com.ruangong.heritage.entity.HeritageItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface HeritageItemMapper extends BaseMapper<HeritageItem> {

    Page<HeritageItem> selectPageWithConditions(Page<HeritageItem> page, @Param("query") HeritageItemListQueryDTO queryDTO);

    List<HeritageItem> searchByKeyWord(String keyword, int limit);
}
