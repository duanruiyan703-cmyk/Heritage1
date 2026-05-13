package com.ruangong.heritage.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruangong.heritage.common.Result;
import com.ruangong.heritage.dto.command.HeritageItemCreateCommandDTO;
import com.ruangong.heritage.dto.command.HeritageItemListQueryDTO;
import com.ruangong.heritage.dto.response.HeritageItemDetailResponseDTO;
import com.ruangong.heritage.service.HeritageItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/heritage-item")
public class HeritageItemController {

    @Autowired
    HeritageItemService heritageItemService;

    @GetMapping("/page")
    public Result<Page<HeritageItemDetailResponseDTO>> getHeritageItemPage(
            Integer currentPage,
             Integer size,
            String title,
             String category,
            String region,
             Integer status,
             String creatorId,
             String startDate,
             String endDate,
            String orderBy,
            String orderDirection
    ){

        HeritageItemListQueryDTO queryDTO = new HeritageItemListQueryDTO();
        queryDTO.setCurrentPage(currentPage);
        queryDTO.setSize(size);
        queryDTO.setTitle(title);
        queryDTO.setCategory(category);
        queryDTO.setRegion(region);
        queryDTO.setStatus(status);
        queryDTO.setCreatorId(creatorId);
        queryDTO.setStartDate(startDate);
        queryDTO.setEndDate(endDate);
        queryDTO.setOrderBy(orderBy);
        queryDTO.setOrderDirection(orderDirection);
        Page<HeritageItemDetailResponseDTO> response=heritageItemService.getHeritageItemPage(queryDTO);
        return Result.success(response);

    }

    // 查看作品
    @RequestMapping("/{itemId}")
    public Result<HeritageItemDetailResponseDTO> getHeritageItemById(@PathVariable String itemId){

        HeritageItemDetailResponseDTO responseDTO=heritageItemService.getHeritageById(itemId);
        return Result.success(responseDTO);
    }

    //
    // 作品的查询
    @RequestMapping("/search")
    public Result<List<HeritageItemDetailResponseDTO>> searchItems(String keyword,int limit){

        List<HeritageItemDetailResponseDTO> responseDTO=heritageItemService.searchItems(keyword,limit);
        return Result.success(responseDTO);
    }


    //非遗作品上传功能
    @RequestMapping("/create")
    public Result<HeritageItemDetailResponseDTO> createHeritageItem(@RequestBody HeritageItemCreateCommandDTO createDTO){

        HeritageItemDetailResponseDTO responseDTO=heritageItemService.createHeritageItem(createDTO);
        return Result.success("上传成功",responseDTO);
    }


    @DeleteMapping("/{itemId}")
    public Result<Void> deleteHeritageItem(@PathVariable String itemId){
        heritageItemService.deleteHeritageItem(itemId);
        return Result.success();

    }


    @RequestMapping ("/{itemId}/publish")
    public Result<Void> publishItem(@PathVariable String itemId){
        heritageItemService.publishHeritageItem(itemId);
        return Result.success();

    }


    @RequestMapping ("/{itemId}/offline")
    public Result<Void> offlineItem(@PathVariable String itemId){
        heritageItemService.offlineHeritageItem(itemId);
        return Result.success();

    }


}
