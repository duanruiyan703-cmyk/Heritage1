package com.ruangong.heritage.service.convert;

import com.ruangong.heritage.dto.command.UserRegisterCommandDTO;
import com.ruangong.heritage.dto.command.UserUpdateCommandDTO;
import com.ruangong.heritage.dto.response.UserDetailResponseDTO;
import com.ruangong.heritage.dto.response.UserLoginResponseDTO;
import com.ruangong.heritage.entity.User;
import com.ruangong.heritage.enums.UserStatus;

import java.time.LocalDateTime;

public class UserConvert {
    public static User registerCommandToEntry(UserRegisterCommandDTO registerDTO, String encodePassword) {
        return User.builder()
                .username(registerDTO.getUsername())
                .email(registerDTO.getEmail())
                .password(encodePassword)
                .name(registerDTO.getName())
                .phone(registerDTO.getPhone())
                .sex(registerDTO.getSex())
                .userType(registerDTO.getUserType())
                .status(UserStatus.NORMAL.getCode()) // 默认正常状态
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public static UserDetailResponseDTO entryToDetailResponse(User user) {
        return UserDetailResponseDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .name(user.getName())
                .avatar(user.getAvatar())
                .phone(user.getPhone())
                .sex(user.getSex())
                .userType(user.getUserType())
                .userTypeDisplayName(user.getUserTypeDisplayName())
                .status(user.getStatus())
                .statusDisplayName(user.getStatusDisplayName())
                .displayName(user.getDisplayName())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    public static UserLoginResponseDTO buildLoginResponse(User user, String token) {

        UserDetailResponseDTO userInfo = entryToDetailResponse(user);
        return UserLoginResponseDTO.builder()
                .userInfo(userInfo)
                .token(token)
                .roleType(userInfo.getUserType())
                .build();
    }

    public static void applyUpdateToEntity(User user, UserUpdateCommandDTO updateDTO) {
        if (updateDTO.getEmail() != null) {
            user.setEmail(updateDTO.getEmail());
        }
        if (updateDTO.getName() != null) {
            user.setName(updateDTO.getName());
        }
        if (updateDTO.getAvatar() != null) {
            user.setAvatar(updateDTO.getAvatar());
        }
        if (updateDTO.getPhone() != null) {
            user.setPhone(updateDTO.getPhone());
        }
        if (updateDTO.getSex() != null) {
            user.setSex(updateDTO.getSex());
        }
        if (updateDTO.getUserType() != null) {
            user.setUserType(updateDTO.getUserType());
        }
        if (updateDTO.getStatus() != null) {
            user.setStatus(updateDTO.getStatus());
        }
        user.setUpdatedAt(LocalDateTime.now());
    }
}
