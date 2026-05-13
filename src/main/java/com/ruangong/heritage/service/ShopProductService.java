package com.ruangong.heritage.service;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruangong.heritage.dto.query.ShopProductListQueryDTO;
import com.ruangong.heritage.dto.response.ShopProductDetailResponseDTO;
import com.ruangong.heritage.dto.response.ShopProductImageResponseDTO;
import com.ruangong.heritage.dto.response.ShopProductListResponseDTO;
import com.ruangong.heritage.entity.ShopCategory;
import com.ruangong.heritage.entity.ShopProduct;
import com.ruangong.heritage.entity.SysFileInfo;
import com.ruangong.heritage.enums.FileBusinessTypeEnum;
import com.ruangong.heritage.exception.BusinessException;
import com.ruangong.heritage.mapper.ShopProductMapper;
import com.ruangong.heritage.mapper.SysFileInfoMapper;
import com.ruangong.heritage.service.convert.ShopProductConvert;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ShopProductService extends ServiceImpl<ShopProductMapper, ShopProduct> {

    @Resource
    private ShopCategoryService shopCategoryService;

    @Resource
    private SysFileInfoMapper sysFileInfoMapper;

    /**
     * 分页查询商品列表
     */
    public Page<ShopProductListResponseDTO> getProductPage(ShopProductListQueryDTO queryDTO) {
        log.info("开始分页查询商品列表，页码: {}, 大小: {}", queryDTO.getPage(), queryDTO.getPageSize());

        Page<ShopProduct> page = new Page<>(queryDTO.getPage(), queryDTO.getPageSize());
        LambdaQueryWrapper<ShopProduct> wrapper = new LambdaQueryWrapper<>();

        // 标题模糊搜索
        if (queryDTO.getTitle() != null && !queryDTO.getTitle().trim().isEmpty()) {
            wrapper.like(ShopProduct::getTitle, queryDTO.getTitle().trim());
        }

        // 分类筛选
        if (queryDTO.getCategoryId() != null) {
            wrapper.eq(ShopProduct::getCategoryId, queryDTO.getCategoryId());
        }

        // 状态筛选
        if (queryDTO.getStatus() != null) {
            wrapper.eq(ShopProduct::getStatus, queryDTO.getStatus());
        }

        // 价格范围筛选
        if (queryDTO.getMinPrice() != null) {
            wrapper.ge(ShopProduct::getPrice, queryDTO.getMinPrice());
        }
        if (queryDTO.getMaxPrice() != null) {
            wrapper.le(ShopProduct::getPrice, queryDTO.getMaxPrice());
        }

        // 库存筛选
        if (queryDTO.getHasStock() != null) {
            if (queryDTO.getHasStock()) {
                wrapper.gt(ShopProduct::getStock, 0);
            } else {
                wrapper.eq(ShopProduct::getStock, 0);
            }
        }

        // 排序
        String sortField = queryDTO.getSortField();
        String sortOrder = queryDTO.getSortOrder();
        if ("price".equals(sortField)) {
            wrapper.orderBy(true, "asc".equalsIgnoreCase(sortOrder), ShopProduct::getPrice);
        } else if ("stock".equals(sortField)) {
            wrapper.orderBy(true, "asc".equalsIgnoreCase(sortOrder), ShopProduct::getStock);
        } else {
            // 默认按创建时间降序（最新在前）
            wrapper.orderByDesc(ShopProduct::getCreateTime);
        }

        Page<ShopProduct> productPage = page(page, wrapper);

        // 转换为响应DTO
        Page<ShopProductListResponseDTO> result = new Page<>();
        result.setCurrent(productPage.getCurrent());
        result.setSize(productPage.getSize());
        result.setTotal(productPage.getTotal());

        List<ShopProductListResponseDTO> responseList = productPage.getRecords().stream()
                .map(ShopProductConvert::entityToListResponse)
                .collect(Collectors.toList());

        // 批量填充分类名称
        fillCategoryNames(responseList);

        // 批量填充封面文件路径
        fillCoverFilePaths(responseList);

        result.setRecords(responseList);

        log.info("分页查询商品列表完成，共{}条记录", result.getTotal());
        return result;
    }

    /**
     * 批量填充分类名称
     */
    private void fillCategoryNames(List<ShopProductListResponseDTO> responseList) {
        if (responseList == null || responseList.isEmpty()) {
            return;
        }

        // 收集所有分类ID
        List<Long> categoryIds = responseList.stream()
                .map(ShopProductListResponseDTO::getCategoryId)
                .filter(id -> id != null)
                .distinct()
                .collect(Collectors.toList());

        if (categoryIds.isEmpty()) {
            return;
        }

        // 批量查询分类
        List<ShopCategory> categories = shopCategoryService.listByIds(categoryIds);
        Map<Long, String> categoryNameMap = categories.stream()
                .collect(Collectors.toMap(ShopCategory::getId, ShopCategory::getName));

        // 填充分类名称
        responseList.forEach(response -> {
            String categoryName = categoryNameMap.get(response.getCategoryId());
            if (categoryName != null) {
                ShopProductConvert.fillCategoryName(response, categoryName);
            }
        });
    }

    /**
     * 批量填充封面文件路径
     */
    private void fillCoverFilePaths(List<ShopProductListResponseDTO> responseList) {
        if (responseList == null || responseList.isEmpty()) {
            return;
        }

        // 收集所有商品ID
        List<String> productIds = responseList.stream()
                .map(ShopProductListResponseDTO::getId)
                .filter(id -> id != null)
                .distinct()
                .collect(Collectors.toList());

        if (productIds.isEmpty()) {
            return;
        }

        // 批量查询封面文件
        List<SysFileInfo> files = sysFileInfoMapper.selectList(
                new LambdaQueryWrapper<SysFileInfo>()
                        .eq(SysFileInfo::getBusinessType, FileBusinessTypeEnum.SHOP_PRODUCT.name())
                        .in(SysFileInfo::getBusinessId, productIds)
                        .eq(SysFileInfo::getBusinessField, "cover")
                        .eq(SysFileInfo::getStatus, 1)
        );

        // 按商品ID分组，每个商品只取最新的一个封面
        Map<String, SysFileInfo> fileMap = files.stream()
                .collect(Collectors.toMap(
                        SysFileInfo::getBusinessId,
                        file -> file,
                        (existing, replacement) ->
                                existing.getCreateTime().isAfter(replacement.getCreateTime()) ? existing : replacement
                ));

        // 填充文件路径和文件ID
        responseList.forEach(response -> {
            SysFileInfo file = fileMap.get(response.getId());
            if (file != null) {
                response.setCoverFileId(file.getId());
                ShopProductConvert.fillCoverFilePath(response, file.getFilePath());
            }
        });
    }

    /**
     * 获取商品详情
     */
    public ShopProductDetailResponseDTO getProductDetailById(String id) {
        log.info("开始获取商品详情，ID: {}", id);

        ShopProduct product = getById(id);
        if (product == null) {
            throw new BusinessException("商品不存在");
        }

        ShopProductDetailResponseDTO response = ShopProductConvert.entityToDetailResponse(product);

        // 填充分类名称
        if (product.getCategoryId() != null) {
            ShopCategory category = shopCategoryService.getById(product.getCategoryId());
            if (category != null) {
                ShopProductConvert.fillCategoryName(response, category.getName());
            }
        }

        // 填充封面文件路径（从文件表查询）
        SysFileInfo coverFile = sysFileInfoMapper.selectOne(
                new LambdaQueryWrapper<SysFileInfo>()
                        .eq(SysFileInfo::getBusinessType, FileBusinessTypeEnum.SHOP_PRODUCT.name())
                        .eq(SysFileInfo::getBusinessId, product.getId())
                        .eq(SysFileInfo::getBusinessField, "cover")
                        .eq(SysFileInfo::getStatus, 1)
                        .orderByDesc(SysFileInfo::getCreateTime)
                        .last("LIMIT 1")
        );
        if (coverFile != null) {
            response.setCoverFileId(coverFile.getId());
            ShopProductConvert.fillCoverFilePath(response, coverFile.getFilePath());
        }

        // 填充商品图片列表（从文件表查询）
        fillProductImages(response, product.getId());

        log.info("获取商品详情成功，ID: {}", id);
        return response;
    }

    /**
     * 填充商品图片列表
     */
    private void fillProductImages(ShopProductDetailResponseDTO response, String productId) {
        if (response == null || productId == null) {
            return;
        }

        // 查询商品的所有图片（business_field='images'）
        List<SysFileInfo> imageFiles = sysFileInfoMapper.selectList(
                new LambdaQueryWrapper<SysFileInfo>()
                        .eq(SysFileInfo::getBusinessType, FileBusinessTypeEnum.SHOP_PRODUCT.name())
                        .eq(SysFileInfo::getBusinessId, productId)
                        .eq(SysFileInfo::getBusinessField, "images")
                        .eq(SysFileInfo::getStatus, 1)

                        .orderByDesc(SysFileInfo::getCreateTime)
        );

        // 转换为 DTO
        List<ShopProductImageResponseDTO> imageList = new ArrayList<>();
        for (SysFileInfo file : imageFiles) {
            ShopProductImageResponseDTO imageDTO = ShopProductImageResponseDTO.builder()
                    .id(file.getId())
                    .filePath(file.getFilePath())
                    .originalName(file.getOriginalName())

                    .build();
            imageList.add(imageDTO);
        }

        response.setImageList(imageList);
        log.debug("填充商品图片列表完成，商品ID: {}, 图片数量: {}", productId, imageList.size());
    }
}

