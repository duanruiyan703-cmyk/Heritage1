package com.ruangong.heritage.service.convert;


import com.ruangong.heritage.dto.response.InheritorDetailResponseDTO;
import com.ruangong.heritage.dto.response.InheritorItemResponseDTO;
import com.ruangong.heritage.dto.response.InheritorResponseDTO;
import com.ruangong.heritage.entity.HeritageItem;
import com.ruangong.heritage.entity.Inheritor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 传承人DTO转换工具类
 * @author system
 */
public class InheritorConvert {



    /**
     * 实体转响应DTO
     */
    public static InheritorResponseDTO entityToResponse(Inheritor inheritor) {
        InheritorResponseDTO response = new InheritorResponseDTO();
        response.setId(inheritor.getId());
        response.setName(inheritor.getName());
        response.setTitle(inheritor.getTitle());
        response.setRegion(inheritor.getRegion());
        response.setBio(inheritor.getBio());
        response.setAvatarFileId(inheritor.getAvatarFileId());
        response.setCreateTime(inheritor.getCreateTime());
        response.setUpdateTime(inheritor.getUpdateTime());
        return response;
    }

    /**
     * 实体转详情响应DTO
     */
    public static InheritorDetailResponseDTO entityToDetailResponse(Inheritor inheritor) {
        InheritorDetailResponseDTO response = new InheritorDetailResponseDTO();
        response.setId(inheritor.getId());
        response.setName(inheritor.getName());
        response.setTitle(inheritor.getTitle());
        response.setRegion(inheritor.getRegion());
        response.setBio(inheritor.getBio());
        response.setAvatarFileId(inheritor.getAvatarFileId());
        response.setCreateTime(inheritor.getCreateTime());
        response.setUpdateTime(inheritor.getUpdateTime());
        return response;
    }
    /**
     * 填充头像路径（详情DTO）
     */
    public static void fillAvatarPathForDetail(InheritorDetailResponseDTO response, String avatarPath) {
        response.setAvatarPath(avatarPath);
    }


    /**
     * 批量填充头像路径
     */
    public static void fillAvatarPathBatch(List<InheritorResponseDTO> responseList, Map<Long, String> avatarPathMap) {
        if (responseList == null || responseList.isEmpty() || avatarPathMap == null) {
            return;
        }
        responseList.forEach(response -> {
            if (response.getAvatarFileId() != null) {
                response.setAvatarPath(avatarPathMap.get(response.getAvatarFileId()));
            }
        });
    }

    /**
     * 非遗作品实体转传承人关联作品响应DTO
     */
    public static InheritorItemResponseDTO heritageItemToInheritorItemResponse(HeritageItem item) {
        InheritorItemResponseDTO response = new InheritorItemResponseDTO();
        response.setId(item.getId());
        response.setTitle(item.getTitle());
        response.setCategory(item.getCategory());
        response.setRegion(item.getRegion());
        response.setSummary(item.getSummary());
        response.setStatus(item.getStatus());
        response.setStatusName(item.getStatusDisplayName());
        return response;
    }

    /**
     * 批量转换非遗作品为传承人关联作品响应DTO
     */
    public static List<InheritorItemResponseDTO> heritageItemListToInheritorItemResponseList(List<HeritageItem> items) {
        if (items == null || items.isEmpty()) {
            return List.of();
        }
        return items.stream()
                .map(InheritorConvert::heritageItemToInheritorItemResponse)
                .collect(Collectors.toList());
    }
}


