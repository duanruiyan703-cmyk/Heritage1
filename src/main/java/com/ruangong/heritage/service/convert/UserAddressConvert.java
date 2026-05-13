package com.ruangong.heritage.service.convert;

import com.ruangong.heritage.dto.command.UserAddressCreateCommandDTO;
import com.ruangong.heritage.dto.command.UserAddressUpdateCommandDTO;
import com.ruangong.heritage.dto.response.UserAddressResponseDTO;
import com.ruangong.heritage.entity.UserAddress;

public class UserAddressConvert {


    public static UserAddressResponseDTO toResponseDTO(UserAddress entity) {
        UserAddressResponseDTO dto = new UserAddressResponseDTO();
        dto.setId(entity.getId());
        dto.setReceiver(entity.getReceiver());
        dto.setPhone(entity.getPhone());
        dto.setProvince(entity.getProvince());
        dto.setCity(entity.getCity());
        dto.setDistrict(entity.getDistrict());
        dto.setDetail(entity.getDetail());
        dto.setFullAddress(entity.getFullAddress());
        dto.setIsDefault(entity.getIsDefault());
        dto.setCreateTime(entity.getCreateTime());
        dto.setUpdateTime(entity.getUpdateTime());

        return dto;

    }

    public static UserAddress toEntity(UserAddressCreateCommandDTO dto, Long userId) {
        UserAddress entity = new UserAddress();
        entity.setUserId(userId);
        entity.setReceiver(dto.getReceiver());
        entity.setPhone(dto.getPhone());
        entity.setProvince(dto.getProvince());
        entity.setCity(dto.getCity());
        entity.setDistrict(dto.getDistrict());
        entity.setDetail(dto.getDetail());
        entity.setIsDefault(dto.getIsDefault() != null && dto.getIsDefault() ? 1 : 0);

        return entity;
    }

    public static void updateEntity(UserAddressUpdateCommandDTO dto, UserAddress entity) {

        entity.setReceiver(dto.getReceiver());
        entity.setPhone(dto.getPhone());
        entity.setProvince(dto.getProvince());
        entity.setCity(dto.getCity());
        entity.setDistrict(dto.getDistrict());
        entity.setDetail(dto.getDetail());
        if (dto.getIsDefault() != null) {
            entity.setIsDefault(dto.getIsDefault() ? 1 : 0);
        }
    }
}
