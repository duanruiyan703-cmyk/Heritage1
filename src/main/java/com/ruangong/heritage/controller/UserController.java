package com.ruangong.heritage.controller;


import com.ruangong.heritage.common.Result;
import com.ruangong.heritage.dto.command.*;
import com.ruangong.heritage.dto.response.UserDetailResponseDTO;
import com.ruangong.heritage.dto.response.UserLoginResponseDTO;
import com.ruangong.heritage.exception.BusinessException;
import com.ruangong.heritage.service.UserService;
import com.ruangong.heritage.util.JwtTokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @RequestMapping("/add")
    public Result<UserDetailResponseDTO> register(@RequestBody UserRegisterCommandDTO userRegisterCommandDTO){
        System.out.println(userRegisterCommandDTO);

        UserDetailResponseDTO responseDTO = userService.register(userRegisterCommandDTO);
        return Result.success("注册成功",responseDTO);
    }


    @RequestMapping("/login")
    public Result<UserLoginResponseDTO> login(@RequestBody UserLoginCommandDTO userLoginCommandDTO){
        UserLoginResponseDTO responseDTO = userService.login(userLoginCommandDTO);
        return Result.success("登录成功",responseDTO);
    }

    //忘记密码
    @RequestMapping("/forget")
    public Result<Void> forget(@RequestBody UserResetPasswordCommandDTO userResetPasswordCommandDTO){
        userService.resetPassword(userResetPasswordCommandDTO);
        return Result.success();
    }


    //忘记密码
    @RequestMapping("/current")
    public Result<UserDetailResponseDTO> current(){
        Result<UserDetailResponseDTO> result=userService.getCurrentUser();
        return result;
    }


    //修改用户信息
    @RequestMapping("/{id}")
    public Result<UserDetailResponseDTO> updateUser(@PathVariable Long id,@RequestBody UserUpdateCommandDTO updateDto){

        try{
            UserDetailResponseDTO currentUser = JwtTokenUtils.getCurrentUser();
            if(currentUser==null){
                throw new BusinessException("未登录或者已过期");
            }
            // 只能自己修改自己的信息，除非你是超级管理员
            if(!id.equals(currentUser.getId()) && !currentUser.getUserType().equals("ADMIN")){
                throw new BusinessException("无权限修改他人的基本信息");
            }
            UserDetailResponseDTO responseDTO =userService.updateUser(id,updateDto);

            return Result.success("更新成功",responseDTO);

        }catch (Exception e){
            return Result.error("更新失败"+e.getMessage());
        }

    }


    @RequestMapping("/password/{id}")
    public Result<Void> resetPassword(@PathVariable Long id,@RequestBody  UserPasswordUpdateCommandDTO passwordUpdateCommandDTO){

        try{
            UserDetailResponseDTO currentUser = JwtTokenUtils.getCurrentUser();
            if(currentUser==null){
                throw new BusinessException("登录失效或者未登录");
            }
            // 只能自己修改自己的信息，除非你是超级管理员
            if(!id.equals(currentUser.getId()) && !currentUser.getUserType().equals("ADMIN")){
                throw new BusinessException("无权限修改他人的基本信息");
            }
            userService.updatePassword(id,passwordUpdateCommandDTO);
            return Result.success();

        }catch (Exception e){
            return Result.error("修改密码失败"+e.getMessage());
        }


    }


}
