package com.ruangong.heritage.service;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruangong.heritage.dto.command.HeritageItemCreateCommandDTO;
import com.ruangong.heritage.dto.command.HeritageItemListQueryDTO;
import com.ruangong.heritage.dto.response.HeritageItemDetailResponseDTO;
import com.ruangong.heritage.dto.response.UserDetailResponseDTO;
import com.ruangong.heritage.entity.HeritageItem;
import com.ruangong.heritage.entity.SysFileInfo;
import com.ruangong.heritage.entity.User;
import com.ruangong.heritage.enums.HeritageItemStatus;
import com.ruangong.heritage.exception.BusinessException;
import com.ruangong.heritage.exception.ServiceException;
import com.ruangong.heritage.mapper.HeritageItemMapper;
import com.ruangong.heritage.mapper.SysFileInfoMapper;
import com.ruangong.heritage.mapper.UserMapper;
import com.ruangong.heritage.service.convert.HeritageItemConvert;
import com.ruangong.heritage.util.JwtTokenUtils;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class HeritageItemService {


    @Resource
    HeritageItemMapper heritageItemMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private SysFileInfoMapper sysFileInfoMapper;


    public Page<HeritageItemDetailResponseDTO> getHeritageItemPage(HeritageItemListQueryDTO queryDTO) {

        Page<HeritageItem> page = new Page<>(queryDTO.getCurrentPage(), queryDTO.getSize());

        System.out.println(heritageItemMapper);

        Page<HeritageItem> itemPage = heritageItemMapper.selectPageWithConditions(page, queryDTO);

        Page<HeritageItemDetailResponseDTO> resultPage = new Page<>(itemPage.getCurrent(), itemPage.getTotal());

        List<HeritageItemDetailResponseDTO> records = itemPage.getRecords().stream().map(HeritageItemConvert::entityToDetailResponse)
                .collect(Collectors.toList());

        // 填充图片以及传承人信息
        fillItemInfoBatch(records);

        resultPage.setRecords(records);

        return resultPage;


    }

    private void fillItemInfoBatch(List<HeritageItemDetailResponseDTO> records) {
        // 判读是否为空
        if (records == null && records.isEmpty()) {
            return;
        }

        // 填充传承人的姓名信息
        List<String> creatorIds = records.stream().map(HeritageItemDetailResponseDTO::getCreatorId)
                .filter(StringUtils::hasText)
                .distinct()
                .collect(Collectors.toList());
        if (!creatorIds.isEmpty()) {
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.in(User::getId, creatorIds);

            List<User> users = userMapper.selectList(queryWrapper);
            // 将这样的信息变为一个map集合
            Map<String, String> creatorNameMap = users.stream().collect(
                    Collectors.toMap(
                            user -> String.valueOf(user.getId()),
                            User::getName,
                            (existing, replacement) -> existing
                    )
            );

            HeritageItemConvert.fillCreatorInfoBatch(records, creatorNameMap);
        }

        // 填充列表的封面信息
        // 收集我们的非遗作品id 为list
        List<String> itemIds = records.stream()
                .map(HeritageItemDetailResponseDTO::getId)
                .filter(StringUtils::hasText)
                .collect(Collectors.toList());
        if (!itemIds.isEmpty()) {
            LambdaQueryWrapper<SysFileInfo> wrapper = new LambdaQueryWrapper<>();
            wrapper.in(SysFileInfo::getBusinessId, itemIds)
                    .eq(SysFileInfo::getBusinessField, "cover")
                    .eq(SysFileInfo::getStatus, 1)
                    .eq(SysFileInfo::getBusinessType, "HERITAGE_ITEM")
                    .orderByDesc(SysFileInfo::getCreateTime);
            List<SysFileInfo> coverFiles
                    = sysFileInfoMapper.selectList(wrapper);

            Map<String, SysFileInfo> sysFileInfoMap = coverFiles.stream().collect(
                    Collectors.toMap(
                            SysFileInfo::getBusinessId,
                            file -> file,
                            (existing, replacement) -> existing.getCreateTime().isAfter(replacement.getCreateTime())
                                    ? existing : replacement
                    )
            );
            // 填充封面的信息
            records.forEach(
                    response -> {
                        SysFileInfo coverFile = sysFileInfoMap.get(response.getId());
                        if (coverFile != null) {
                            response.setCoverFileId(coverFile.getId());
                            response.setCoverImage(coverFile.getFilePath());
                        }

                    }

            );

        }


    }

    public HeritageItemDetailResponseDTO getHeritageById(String itemId) {

        try {
            HeritageItem heritageItem = heritageItemMapper.selectById(itemId);
            if (heritageItem == null) {
                throw new BusinessException("该作品不存在");
            }

            HeritageItemDetailResponseDTO responseDTO = HeritageItemConvert.entityToDetailResponse(heritageItem);

            fillItemInfo(responseDTO);

            return responseDTO;
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("查看作品失败，请重试" + e.getMessage());
        }

    }

    private void fillItemInfo(HeritageItemDetailResponseDTO responseDTO) {
        // 判断是否为空
        if (responseDTO == null) {
            return;
        }

        // 填充创建人信息


        User user = userMapper.selectById(responseDTO.getCreatorId());
        responseDTO.setCreatorName(user.getName());

        // 填充封面信息
        // 填充封面信息 - 从 sys_file_info 表查询 business_field='cover' 的文件
        LambdaQueryWrapper<SysFileInfo> coverQuery = new LambdaQueryWrapper<>();
        coverQuery.eq(SysFileInfo::getBusinessType, "HERITAGE_ITEM")
                .eq(SysFileInfo::getBusinessId, responseDTO.getId())
                .eq(SysFileInfo::getBusinessField, "cover")
                .eq(SysFileInfo::getStatus, 1) // 只查询正常状态的文件
                .orderByDesc(SysFileInfo::getCreateTime) // 按创建时间倒序，取最新的封面
                .last("LIMIT 1");

        SysFileInfo coverFile = sysFileInfoMapper.selectOne(coverQuery);
        if (coverFile != null) {
            responseDTO.setCoverFileId(coverFile.getId());
            responseDTO.setCoverImage(coverFile.getFilePath());
        }

        // 填充媒体信息 - 直接从 sys_file_info 表查询 business_field='media' 的文件
        LambdaQueryWrapper<SysFileInfo> fileQuery = new LambdaQueryWrapper<>();
        fileQuery.eq(SysFileInfo::getBusinessType, "HERITAGE_ITEM")
                .eq(SysFileInfo::getBusinessId, responseDTO.getId())
                .eq(SysFileInfo::getBusinessField, "media")
                .eq(SysFileInfo::getStatus, 1) // 只查询正常状态的文件
                .orderByAsc(SysFileInfo::getCreateTime); // 按创建时间排序

        List<SysFileInfo> fileInfoList = sysFileInfoMapper.selectList(fileQuery);
        if (!fileInfoList.isEmpty()) {
            responseDTO.setMediaList(HeritageItemConvert.fileInfoListToMediaResponseList(fileInfoList));
        }
    }

    public List<HeritageItemDetailResponseDTO> searchItems(String keyword, int limit) {
        List<HeritageItem> list = heritageItemMapper.searchByKeyWord(keyword, limit);
        // 将 list 转换为 List<HeritageItemDetailResponseDTO>
        List<HeritageItemDetailResponseDTO> result = list.stream().map(HeritageItemConvert::entityToDetailResponse).collect(Collectors.toList());
        // 填充人物以及图片等信息
        fillItemInfoBatch(result);

        return result;
    }

    @Transactional()
    public HeritageItemDetailResponseDTO createHeritageItem(HeritageItemCreateCommandDTO createDTO) {
        try {

            UserDetailResponseDTO currentUser = JwtTokenUtils.getCurrentUser();
            if (currentUser == null) {
                throw new BusinessException("未登录或者登录失效");
            }
            String id = createDTO.getId();
            if (!StringUtils.hasText(id)) {
                throw new BusinessException("UUID不能为空");
            }
            // 该id 在作品集中是否存在
            HeritageItem heritageItem = heritageItemMapper.selectById(id);
            if (heritageItem != null) {
                throw new BusinessException("该UUID已存在，请重新生成一个");
            }

            // 判断作品的状态是否是规定的那几个状态
            if (createDTO.getStatus() != null && !HeritageItemStatus.isValidCode(createDTO.getStatus())) {
                throw new BusinessException("该作品的状态无效");
            }

            HeritageItem item = HeritageItemConvert.createCommandToEntity(createDTO, currentUser.getId());

            heritageItemMapper.insert(item);

            // 根据id 将作品查询出来，然后转换成 responseDTO
            HeritageItem item2 = heritageItemMapper.selectById(item.getId());
            HeritageItemDetailResponseDTO responseDTO = HeritageItemConvert.entityToDetailResponse(item2);
            // 填充图片等信息
            fillItemInfo(responseDTO);

            return responseDTO;


        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("上传失败，请重试" + e.getMessage());
        }


    }

    public void deleteHeritageItem(String itemId) {

        try {
            HeritageItem item = heritageItemMapper.selectById(itemId);
            if (item == null) {
                throw new BusinessException("作品不存在");
            }

            // 权限检查：只有创建人或管理员可以删除
            String currentUserId = JwtTokenUtils.getCurrentUserIdAsString();
            if (!item.getCreatorId().equals(currentUserId) && !JwtTokenUtils.isAdmin()) {
                throw new BusinessException("无权限删除此作品");
            }

            // 状态检查：只有草稿和下架状态可以删除
            HeritageItemStatus currentStatus = HeritageItemStatus.fromCode(item.getStatus());
            if (currentStatus != null && !currentStatus.canDelete()) {
                throw new BusinessException("当前状态不允许删除");
            }

            heritageItemMapper.deleteById(itemId);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("上传失败，请重试" + e.getMessage());
        }
    }

    public void publishHeritageItem(String itemId) {

        try {
            HeritageItem item = heritageItemMapper.selectById(itemId);
            if (item == null) {
                throw new BusinessException("作品不存在");
            }

            // 权限检查：只有创建人或管理员可以删除
            String currentUserId = JwtTokenUtils.getCurrentUserIdAsString();
            if (!item.getCreatorId().equals(currentUserId) && !JwtTokenUtils.isAdmin()) {
                throw new BusinessException("不是管理员，无权限发布此作品");
            }

            // 状态检查：只有草稿和下架状态可以删除
            HeritageItemStatus currentStatus = HeritageItemStatus.fromCode(item.getStatus());
            if (currentStatus != null && !currentStatus.canPublish()) {
                throw new BusinessException("当前状态不允许发布");
            }

            item.setStatus(HeritageItemStatus.PUBLISHED.getCode());
            item.setUpdateTime(LocalDateTime.now());

            heritageItemMapper.updateById(item);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("发布失败，请重试" + e.getMessage());
        }
    }

    public void offlineHeritageItem(String itemId) {
        try {
            HeritageItem item = heritageItemMapper.selectById(itemId);
            if (item == null) {
                throw new BusinessException("作品不存在");
            }

            // 权限检查：只有创建人或管理员可以删除
            String currentUserId = JwtTokenUtils.getCurrentUserIdAsString();
            if (!item.getCreatorId().equals(currentUserId) && !JwtTokenUtils.isAdmin()) {
                throw new BusinessException("不是管理员，无权限下架此作品");
            }

            // 状态检查：只有草稿和下架状态可以删除
            HeritageItemStatus currentStatus = HeritageItemStatus.fromCode(item.getStatus());
            if (currentStatus != null && !currentStatus.canOffline()) {
                throw new BusinessException("当前状态不允许下架");
            }

            item.setStatus(HeritageItemStatus.OFFLINE.getCode());
            item.setUpdateTime(LocalDateTime.now());

            heritageItemMapper.updateById(item);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("下架失败，请重试" + e.getMessage());
        }
    }
}
