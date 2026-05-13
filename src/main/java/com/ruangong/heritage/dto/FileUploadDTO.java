package com.ruangong.heritage.dto;

import lombok.Data;

@Data
//文件上传请求DTO
public class FileUploadDTO {

    //业务类型
    private String businessType;


    //业务对象ID
    private String businessId;

    //业务字段名"
    private String businessField;

    //是否临时文件
    private Boolean isTemp;
}
