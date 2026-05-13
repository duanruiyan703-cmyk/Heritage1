package com.ruangong.heritage.service.convert;


import com.ruangong.heritage.dto.response.ShopProductDetailResponseDTO;
import com.ruangong.heritage.dto.response.ShopProductListResponseDTO;
import com.ruangong.heritage.entity.ShopProduct;

public class ShopProductConvert {

    /**
     * 实体转列表响应DTO
     */
    public static ShopProductListResponseDTO entityToListResponse(ShopProduct product) {
        if (product == null) {
            return null;
        }

        ShopProductListResponseDTO response = new ShopProductListResponseDTO();
        response.setId(product.getId());
        response.setTitle(product.getTitle());
        response.setSubtitle(product.getSubtitle());
        response.setCategoryId(product.getCategoryId());
        response.setPrice(product.getPrice());
        response.setStock(product.getStock());
        response.setStatus(product.getStatus());
        response.setStatusName(product.getStatusDisplayName());
        response.setCreateTime(product.getCreateTime());
        response.setUpdateTime(product.getUpdateTime());

        return response;
    }

    /**
     * 填充分类名称（列表）
     */
    public static void fillCategoryName(ShopProductListResponseDTO response, String categoryName) {
        if (response != null) {
            response.setCategoryName(categoryName);
        }
    }

    /**
     * 填充封面文件路径（列表）
     */
    public static void fillCoverFilePath(ShopProductListResponseDTO response, String filePath) {
        if (response != null) {
            response.setCoverFilePath(filePath);
        }
    }

    /**
     * 实体转详情响应DTO
     */
    public static ShopProductDetailResponseDTO entityToDetailResponse(ShopProduct product) {
        if (product == null) {
            return null;
        }

        ShopProductDetailResponseDTO response = new ShopProductDetailResponseDTO();
        response.setId(product.getId());
        response.setTitle(product.getTitle());
        response.setSubtitle(product.getSubtitle());
        response.setCategoryId(product.getCategoryId());
        response.setPrice(product.getPrice());
        response.setStock(product.getStock());
        response.setDetail(product.getDetail());
        response.setStatus(product.getStatus());
        response.setStatusName(product.getStatusDisplayName());
        response.setCreateTime(product.getCreateTime());
        response.setUpdateTime(product.getUpdateTime());

        return response;
    }

    /**
     * 填充封面文件路径
     */
    public static void fillCoverFilePath(ShopProductDetailResponseDTO response, String filePath) {
        if (response != null) {
            response.setCoverFilePath(filePath);
        }
    }


    /**
     * 填充分类名称
     */
    public static void fillCategoryName(ShopProductDetailResponseDTO response, String categoryName) {
        if (response != null) {
            response.setCategoryName(categoryName);
        }
    }
}

