package com.ruangong.heritage.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruangong.heritage.common.Result;
import com.ruangong.heritage.dto.query.ShopProductListQueryDTO;
import com.ruangong.heritage.dto.response.ShopProductDetailResponseDTO;
import com.ruangong.heritage.dto.response.ShopProductListResponseDTO;
import com.ruangong.heritage.service.ShopProductService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 商品控制器
 * @author system
 */
@Tag(name = "商品管理")
@RestController
@RequestMapping("/shop/product")
@Slf4j
public class ShopProductController {

    @Resource
    private ShopProductService shopProductService;



    //分页查询商品列表
    @GetMapping("/page")
    public Result<Page<ShopProductListResponseDTO>> getProductPage(ShopProductListQueryDTO queryDTO) {
        log.info("分页查询商品列表，页码: {}, 大小: {}", queryDTO.getPage(), queryDTO.getPageSize());
        Page<ShopProductListResponseDTO> result = shopProductService.getProductPage(queryDTO);
        return Result.success(result);
    }

    //"获取商品详情"
    @GetMapping("/{id}")
    public Result<ShopProductDetailResponseDTO> getProductDetail(@PathVariable String id) {
        log.info("获取商品详情，ID: {}", id);
        ShopProductDetailResponseDTO result = shopProductService.getProductDetailById(id);
        return Result.success(result);
    }

}


