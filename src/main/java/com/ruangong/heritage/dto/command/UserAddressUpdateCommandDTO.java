package com.ruangong.heritage.dto.command;

import lombok.Data;

@Data
public class UserAddressUpdateCommandDTO {

    private String receiver;
    private String phone;
    private String province;
    private String city;
    private String district;
    private String detail;
    private Boolean isDefault;
}
