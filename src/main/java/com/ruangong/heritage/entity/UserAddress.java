package com.ruangong.heritage.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserAddress {

    private Long id;


    private Long userId;


    private String receiver;


    private String phone;


    private String province;


    private String city;


    private String district;


    private String detail;


    private Integer isDefault;


    private LocalDateTime createTime;


    private LocalDateTime updateTime;

    /**
     * 获取完整地址
     */
    public String getFullAddress() {
        return province + city + district + detail;
    }


}
