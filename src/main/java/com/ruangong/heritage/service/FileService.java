package com.ruangong.heritage.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruangong.heritage.dto.FileInfoDTO;
import com.ruangong.heritage.dto.FileUploadDTO;
import com.ruangong.heritage.entity.SysFileInfo;
import com.ruangong.heritage.enums.FileBusinessTypeEnum;
import com.ruangong.heritage.enums.FileTypeEnum;
import com.ruangong.heritage.mapper.SysFileInfoMapper;
import com.ruangong.heritage.service.convert.FileConvert;
import com.ruangong.heritage.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Slf4j
public class FileService {

    @Autowired
    SysFileInfoMapper sysFileInfoMapper;

    private final static  String BUSSINESS_FILE_RELATIVE_PATH = "bussiness";

    public FileInfoDTO uploadFile(MultipartFile file, FileUploadDTO uploadDTO, Long userId, boolean replaceOld) {
        log.info("开始上传文件: 文件名={}, 业务类型={}, 业务ID={}, 替换模式={}",
                file.getOriginalFilename(), uploadDTO.getBusinessType(), uploadDTO.getBusinessId(), replaceOld);
        // 3. 如果需要替换，先处理旧文件
        if (replaceOld) {
            // 此处可以编写一些删除旧图片的一些操作
        }
        // 保存文件到磁盘上(其实就是文件夹里面)
        String filePath = FileUtil.saveFile(file, BUSSINESS_FILE_RELATIVE_PATH, FileUtil.parseBussinessFileTypeToFolerName(uploadDTO.getBusinessType()));
        // 保存信息到数据库里面  sys_file_info
        SysFileInfo sysFileInfo = createFileInfo(file,uploadDTO,filePath,userId);
        sysFileInfoMapper.insert(sysFileInfo);

        return FileConvert.convertToDTO(sysFileInfo);
    }

    private SysFileInfo createFileInfo(MultipartFile file, FileUploadDTO uploadDTO, String filePath, Long userId) {

        SysFileInfo fileInfo = new SysFileInfo();
        fileInfo.setOriginalName(file.getOriginalFilename());
        fileInfo.setFilePath(filePath);
        fileInfo.setFileSize(file.getSize());
        fileInfo.setFileType(FileTypeEnum.getByFileName(file.getOriginalFilename()).getCode());
        fileInfo.setBusinessType(uploadDTO.getBusinessType());
        fileInfo.setBusinessId(uploadDTO.getBusinessId());
        fileInfo.setBusinessField(uploadDTO.getBusinessField());
        fileInfo.setUploadUserId(userId);
        fileInfo.setIsTemp(uploadDTO.getIsTemp() != null && uploadDTO.getIsTemp() ? 1 : 0);
        fileInfo.setStatus(1);
        // createTime 由 MyBatis-Plus 自动填充，无需手动设置
        return fileInfo;
    }

    /**
     * 获取业务字段的文件
     */
    public List<FileInfoDTO> getFilesByBusinessField(String businessType, String businessId, String businessField) {
        log.info("查询业务字段文件: 业务类型={}, 业务ID={}, 字段={}", businessType, businessId, businessField);

        LambdaQueryWrapper<SysFileInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysFileInfo::getBusinessType, businessType)
                .eq(SysFileInfo::getBusinessId, businessId)
                .eq(SysFileInfo::getBusinessField, businessField)
                .eq(SysFileInfo::getStatus, 1)
                .orderByDesc(SysFileInfo::getCreateTime);

        List<SysFileInfo> fileList = sysFileInfoMapper.selectList(queryWrapper);
        return convertToDTOList(fileList);
    }

    /**
     * 转换为DTO列表
     */
    private List<FileInfoDTO> convertToDTOList(List<SysFileInfo> fileList) {
        return fileList.stream().map(this::convertToDTO).toList();
    }

    /**
     * 转换为DTO
     */
    private FileInfoDTO convertToDTO(SysFileInfo fileInfo) {
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
