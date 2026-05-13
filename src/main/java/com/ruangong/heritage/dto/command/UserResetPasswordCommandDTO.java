package com.ruangong.heritage.dto.command;


import lombok.Data;

@Data
public class UserResetPasswordCommandDTO {
    private String username;


    private String email;


    private String phone;


    private String newPassword;
}
