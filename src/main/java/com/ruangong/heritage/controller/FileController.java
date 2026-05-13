package com.ruangong.heritage.controller;

import com.ruangong.heritage.common.Result;
import com.ruangong.heritage.dto.FileInfoDTO;
import com.ruangong.heritage.dto.FileUploadDTO;
import com.ruangong.heritage.dto.response.UserDetailResponseDTO;
import com.ruangong.heritage.service.FileService;
import com.ruangong.heritage.util.JwtTokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Slf4j
@RequestMapping("/file")
public class FileController {

    @Autowired
    FileService fileService;

    @RequestMapping("/upload")
    public Result<FileInfoDTO> uploadFile(
             MultipartFile file,
             String businessType,
            String businessId,
            String businessField,
            boolean replaceOld
    ){


        // 将页面上传递过来的信息封装成一个实体
        FileUploadDTO fileUploadDTO = buildFileUploadDTO(businessType,businessId,businessField,false);

        UserDetailResponseDTO currentUser = JwtTokenUtils.getCurrentUser();
        FileInfoDTO fileInfoDTO=fileService.uploadFile(file,fileUploadDTO,currentUser.getId(),replaceOld);

        return Result.success("上传成功",fileInfoDTO);

    }

    private FileUploadDTO buildFileUploadDTO(String businessType, String businessId, String businessField, boolean b) {
        FileUploadDTO uploadDTO = new FileUploadDTO();
        uploadDTO.setBusinessType(businessType);
        uploadDTO.setBusinessId(businessId);
        uploadDTO.setBusinessField(businessField);
        uploadDTO.setIsTemp(b);
        return uploadDTO;
    }
}
