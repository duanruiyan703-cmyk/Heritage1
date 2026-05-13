package com.ruangong.heritage.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDetailResponseDTO {

    private Long id;


    private String username;


    private String email;


    private String name;


    private String avatar;


    private String phone;


    private String sex;


    private String userType;


    private String userTypeDisplayName;


    private Integer status;


    private String statusDisplayName;


    private String displayName;


    private LocalDateTime createdAt;


    private LocalDateTime updatedAt;
}
