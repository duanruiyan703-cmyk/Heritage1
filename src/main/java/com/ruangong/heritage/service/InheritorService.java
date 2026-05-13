package com.ruangong.heritage.service;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruangong.heritage.dto.response.InheritorDetailResponseDTO;
import com.ruangong.heritage.dto.response.InheritorResponseDTO;
import com.ruangong.heritage.entity.HeritageItem;
import com.ruangong.heritage.entity.Inheritor;
import com.ruangong.heritage.entity.InheritorItem;
import com.ruangong.heritage.entity.SysFileInfo;
import com.ruangong.heritage.exception.BusinessException;
import com.ruangong.heritage.exception.ServiceException;
import com.ruangong.heritage.mapper.HeritageItemMapper;
import com.ruangong.heritage.mapper.InheritorItemMapper;
import com.ruangong.heritage.mapper.InheritorMapper;
import com.ruangong.heritage.mapper.SysFileInfoMapper;
import com.ruangong.heritage.service.convert.InheritorConvert;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 传承人业务逻辑层
 * @author system
 */
@Slf4j
@Service
public class InheritorService {

    @Resource
    private InheritorMapper inheritorMapper;

    @Resource
    private SysFileInfoMapper sysFileInfoMapper;

    @Resource
    private HeritageItemMapper heritageItemMapper;
    @Resource
    private InheritorItemMapper inheritorItemMapper;



    /**
     * 分页查询传承人列表
     * @param current 当前页
     * @param size 每页大小
     * @param name 姓名关键词
     * @param title 称号
     * @param region 地区
     * @return 传承人分页列表
     */
    public Page<InheritorResponseDTO> getInheritorPage(Long current, Long size, String name, String title, String region) {
        try {
            Page<Inheritor> page = new Page<>(current, size);

            LambdaQueryWrapper<Inheritor> wrapper = new LambdaQueryWrapper<>();
            if (StringUtils.hasText(name)) {
                wrapper.like(Inheritor::getName, name);
            }
            if (StringUtils.hasText(title)) {
                wrapper.like(Inheritor::getTitle, title);
            }
            if (StringUtils.hasText(region)) {
                wrapper.like(Inheritor::getRegion, region);
            }
            wrapper.orderByDesc(Inheritor::getCreateTime);

            Page<Inheritor> inheritorPage = inheritorMapper.selectPage(page, wrapper);

            // 转换为响应DTO
            Page<InheritorResponseDTO> resultPage = new Page<>(
                    inheritorPage.getCurrent(), inheritorPage.getSize(), inheritorPage.getTotal());

            List<InheritorResponseDTO> records = inheritorPage.getRecords().stream()
                    .map(InheritorConvert::entityToResponse)
                    .collect(Collectors.toList());

            // 批量填充头像路径
            fillAvatarPathBatch(records);

            resultPage.setRecords(records);
            return resultPage;

        } catch (Exception e) {
            log.error("查询传承人列表失败", e);
            throw new ServiceException("查询失败，请稍后重试");
        }
    }


    /**
     * 批量填充头像路径
     */
    private void fillAvatarPathBatch(List<InheritorResponseDTO> responseList) {
        if (responseList == null || responseList.isEmpty()) {
            return;
        }

        // 获取所有头像文件ID
        List<Long> avatarFileIds = responseList.stream()
                .map(InheritorResponseDTO::getAvatarFileId)
                .filter(id -> id != null)
                .distinct()
                .collect(Collectors.toList());

        if (avatarFileIds.isEmpty()) {
            return;
        }

        // 批量查询头像文件信息
        LambdaQueryWrapper<SysFileInfo> fileQuery = new LambdaQueryWrapper<>();
        fileQuery.in(SysFileInfo::getId, avatarFileIds);
        List<SysFileInfo> fileInfoList = sysFileInfoMapper.selectList(fileQuery);

        Map<Long, String> avatarPathMap = fileInfoList.stream()
                .collect(Collectors.toMap(
                        SysFileInfo::getId,
                        SysFileInfo::getFilePath,
                        (existing, replacement) -> existing
                ));

        // 填充头像路径
        InheritorConvert.fillAvatarPathBatch(responseList, avatarPathMap);
    }


    /**
     * 根据ID获取传承人详情
     * @param inheritorId 传承人ID
     * @return 传承人详情
     */
    public InheritorDetailResponseDTO getInheritorById(String inheritorId) {
        try {
            Inheritor inheritor = inheritorMapper.selectById(inheritorId);
            if (inheritor == null) {
                throw new BusinessException("传承人不存在");
            }

            InheritorDetailResponseDTO response = InheritorConvert.entityToDetailResponse(inheritor);

            // 填充关联信息
            fillInheritorInfo(response);

            return response;

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("查询传承人详情失败: inheritorId={}", inheritorId, e);
            throw new ServiceException("查询失败，请稍后重试");
        }
    }


    /**
     * 填充传承人关联信息
     */
    private void fillInheritorInfo(InheritorDetailResponseDTO response) {
        if (response == null) {
            return;
        }

        // 填充头像路径
        if (response.getAvatarFileId() != null) {
            SysFileInfo avatarFile = sysFileInfoMapper.selectById(response.getAvatarFileId());
            if (avatarFile != null) {
                InheritorConvert.fillAvatarPathForDetail(response, avatarFile.getFilePath());
            }
        }

        // 填充关联作品
        List<HeritageItem> relatedItems = getRelatedItems(response.getId());
        response.setHeritageItems(InheritorConvert.heritageItemListToInheritorItemResponseList(relatedItems));
    }

    /**
     * 查询传承人的关联作品列表
     * @param inheritorId 传承人ID
     * @return 关联作品列表
     */
    public List<HeritageItem> getRelatedItems(String inheritorId) {
        try {
            // 查询关联关系
            LambdaQueryWrapper<InheritorItem> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(InheritorItem::getInheritorId, inheritorId);
            List<InheritorItem> relations = inheritorItemMapper.selectList(wrapper);

            if (relations.isEmpty()) {
                return List.of();
            }

            // 查询作品详情
            List<String> itemIds = relations.stream()
                    .map(InheritorItem::getItemId)
                    .collect(Collectors.toList());

            LambdaQueryWrapper<HeritageItem> itemWrapper = new LambdaQueryWrapper<>();
            itemWrapper.in(HeritageItem::getId, itemIds)
                    .orderByDesc(HeritageItem::getCreateTime);

            return heritageItemMapper.selectList(itemWrapper);

        } catch (Exception e) {
            log.error("查询传承人关联作品失败: inheritorId={}", inheritorId, e);
            throw new ServiceException("查询失败，请稍后重试");
        }
    }


}

