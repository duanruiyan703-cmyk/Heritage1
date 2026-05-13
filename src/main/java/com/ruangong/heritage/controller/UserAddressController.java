package com.ruangong.heritage.controller;


import com.ruangong.heritage.common.Result;
import com.ruangong.heritage.dto.command.UserAddressCreateCommandDTO;
import com.ruangong.heritage.dto.command.UserAddressUpdateCommandDTO;
import com.ruangong.heritage.dto.response.UserAddressResponseDTO;
import com.ruangong.heritage.service.UserAddressService;
import com.ruangong.heritage.util.JwtTokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/address")
@Slf4j
public class UserAddressController {

    @Autowired
    UserAddressService userAddressService;

    @RequestMapping("/list")
    public Result<List<UserAddressResponseDTO>> getUserAddressList(){
        Long userId = JwtTokenUtils.getCurrentUserId();
        List<UserAddressResponseDTO> list =userAddressService.getUserAddressListById(userId);
        return Result.success(list);
    }

    @PostMapping
    public Result<UserAddressResponseDTO> createNewUserAddress(@RequestBody UserAddressCreateCommandDTO createCommandDTO){

        //获取当前用户
        Long userId = JwtTokenUtils.getCurrentUserId();

        UserAddressResponseDTO responseDTO =userAddressService.createAddress(createCommandDTO,userId);
        return Result.success(responseDTO);
    }

    @RequestMapping("/{id}/default")
    public Result<Void> setDefaultAddress(@PathVariable Long id){
        Long userId = JwtTokenUtils.getCurrentUserId();
        userAddressService.setDefaultAddress(id,userId);
        return Result.success();
    }

    @PutMapping("/{id}")
    public Result<UserAddressResponseDTO> updateAddress(@PathVariable Long id, @RequestBody UserAddressUpdateCommandDTO updateDTO){

        Long userId = JwtTokenUtils.getCurrentUserId();
        UserAddressResponseDTO responseDTO =userAddressService.updateAddress(id,updateDTO,userId);
        return Result.success(responseDTO);
    }


    @DeleteMapping("/{id}")
    public Result<Void> deleteAddress(@PathVariable Long id){
        Long userId = JwtTokenUtils.getCurrentUserId();
        userAddressService.deleteAddress(id,userId);

        return Result.success();
    }
}
