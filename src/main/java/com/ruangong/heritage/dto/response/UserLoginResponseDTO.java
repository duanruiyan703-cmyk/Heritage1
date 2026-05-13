package com.ruangong.heritage.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginResponseDTO {

    private UserDetailResponseDTO userInfo;

    private String token;

    private String roleType;
}
