package com.ruangong.heritage.service.convert;

import com.ruangong.heritage.dto.FileInfoDTO;
import com.ruangong.heritage.entity.SysFileInfo;
import com.ruangong.heritage.enums.FileBusinessTypeEnum;
import com.ruangong.heritage.enums.FileTypeEnum;

public class FileConvert {


    public static FileInfoDTO convertToDTO(SysFileInfo fileInfo) {
        FileInfoDTO dto = new FileInfoDTO();
        dto.setId(fileInfo.getId());
        dto.setOriginalName(fileInfo.getOriginalName());
        dto.setFilePath(fileInfo.getFilePath());
        dto.setFileSize(fileInfo.getFileSize());
        dto.setFileType(fileInfo.getFileType());
        dto.setBusinessType(fileInfo.getBusinessType());
        dto.setBusinessId(fileInfo.getBusinessId());
        dto.setBusinessField(fileInfo.getBusinessField());
        dto.setUploadUserId(fileInfo.getUploadUserId());
        dto.setIsTemp(fileInfo.isTempFile());
        dto.setStatus(fileInfo.getStatus());
        dto.setCreateTime(fileInfo.getCreateTime());
        dto.setExpireTime(fileInfo.getExpireTime());
        dto.setFileExtension(fileInfo.getFileExtension());
        dto.setIsExpired(fileInfo.isExpired());

        // 设置描述信息
        for (FileTypeEnum fileType : FileTypeEnum.values()) {
            if (fileType.getCode().equals(fileInfo.getFileType())) {
                dto.setFileTypeDesc(fileType.getDesc());
                break;
            }
        }

        FileBusinessTypeEnum businessType = FileBusinessTypeEnum.getByCode(fileInfo.getBusinessType());
        if (businessType != null) {
            dto.setBusinessTypeDesc(businessType.getDesc());
        }

        return dto;
    }
}
