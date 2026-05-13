package com.ruangong.heritage.dto.command;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserAddressCreateCommandDTO {
    //收货人
    private String receiver;


    private String phone;


    private String province;


    private String city;


    private String district;


    private String detail;


    private Boolean isDefault;
}
