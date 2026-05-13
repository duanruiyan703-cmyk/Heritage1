package com.ruangong.heritage.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserAddressResponseDTO {

    private Long id;

    private String receiver;

    private String phone;

    private String province;

    private String city;

    private String district;

    private String detail;

    private String fullAddress;

    private Integer isDefault;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
