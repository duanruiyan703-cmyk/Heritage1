package com.ruangong.heritage.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.ruangong.heritage.dto.command.UserAddressCreateCommandDTO;
import com.ruangong.heritage.dto.command.UserAddressUpdateCommandDTO;
import com.ruangong.heritage.dto.response.UserAddressResponseDTO;
import com.ruangong.heritage.entity.UserAddress;
import com.ruangong.heritage.exception.BusinessException;
import com.ruangong.heritage.mapper.UserAddressMapper;
import com.ruangong.heritage.service.convert.UserAddressConvert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserAddressService {


    @Autowired
    UserAddressMapper userAddressMapper;


    public List<UserAddressResponseDTO> getUserAddressListById(Long userId) {
        LambdaQueryWrapper<UserAddress> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserAddress::getUserId,userId);
        queryWrapper.orderByDesc(UserAddress::getIsDefault);
        queryWrapper.orderByDesc(UserAddress::getCreateTime);

        List<UserAddress> list = userAddressMapper.selectList(queryWrapper);
        List<UserAddressResponseDTO> list2=new ArrayList<>();
        for (UserAddress userAddress : list) {
            list2.add(UserAddressConvert.toResponseDTO(userAddress));
        }

        return list2;

    }

    public UserAddressResponseDTO createAddress(UserAddressCreateCommandDTO createCommandDTO, Long userId) {
        // 如果你添加的是默认收获地址，需要先将以前的默认收获地址修改一下
        if(createCommandDTO.getIsDefault()!=null && createCommandDTO.getIsDefault()){
            //取消以前的默认收获地址了
            unsetUserDefaultAddress(userId);
        }
        UserAddress userAddress =UserAddressConvert.toEntity(createCommandDTO,userId);
        userAddressMapper.insert(userAddress);
        return UserAddressConvert.toResponseDTO(userAddress);
    }

    private void unsetUserDefaultAddress(Long userId) {

        LambdaUpdateWrapper<UserAddress> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(UserAddress::getUserId,userId);
        updateWrapper.eq(UserAddress::getIsDefault,1);
        updateWrapper.set(UserAddress::getIsDefault,0);

        userAddressMapper.update(null,updateWrapper);

    }

    public void setDefaultAddress(Long id, Long userId) {
        UserAddress userAddress = userAddressMapper.selectById(id);
        if(userAddress==null){
            throw new BusinessException("收获地址不存在");
        }
        if(!userAddress.getUserId().equals(userId)){
            throw new BusinessException("暂无权限修改别人的收获地址");
        }
        // 将以前的默认收获地址变为为非默认
        unsetUserDefaultAddress(userId);

        userAddress.setIsDefault(1);
        userAddressMapper.updateById(userAddress);
    }

    public UserAddressResponseDTO updateAddress(Long userAddressId,UserAddressUpdateCommandDTO updateDTO, Long userId) {

        UserAddress userAddress = userAddressMapper.selectById(userAddressId);
        if(userAddress==null){
            throw new BusinessException("收获地址不存在");
        }
        if(!userAddress.getUserId().equals(userId)){
            throw new BusinessException("暂无权限修改别人的收获地址");
        }

        // 将以前的默认收获地址变为为非默认
        unsetUserDefaultAddress(userId);

        UserAddressConvert.updateEntity(updateDTO,userAddress);
        userAddressMapper.updateById(userAddress);
        return UserAddressConvert.toResponseDTO(userAddress);
    }

    public void deleteAddress(Long userAddressId, Long userId) {
        UserAddress userAddress = userAddressMapper.selectById(userAddressId);
        if(userAddress==null){
            throw new BusinessException("收获地址不存在");
        }
        if(!userAddress.getUserId().equals(userId)){
            throw new BusinessException("暂无权限修改别人的收获地址");
        }

        userAddressMapper.deleteById(userAddress);

    }
}
