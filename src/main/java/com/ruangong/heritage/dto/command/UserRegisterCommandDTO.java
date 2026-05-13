package com.ruangong.heritage.dto.command;

import lombok.Data;

@Data
public class UserRegisterCommandDTO {

    private String username;


    private String email;


    private String password;


    private String confirmPassword;


    private String name;


    private String phone;

    private String sex;

    private String userType = "USER";
}
