package com.ruangong.heritage.dto.command;

import lombok.Data;

@Data
public class UserUpdateCommandDTO {


    private String email;


    private String name;

    //头像
    private String avatar;


    private String phone;

    private String sex;

    private String userType;

    private Integer status;
}