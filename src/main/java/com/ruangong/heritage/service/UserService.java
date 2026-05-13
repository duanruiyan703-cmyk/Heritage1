package com.ruangong.heritage.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruangong.heritage.common.Result;
import com.ruangong.heritage.dto.command.*;
import com.ruangong.heritage.dto.response.UserDetailResponseDTO;
import com.ruangong.heritage.dto.response.UserLoginResponseDTO;
import com.ruangong.heritage.entity.User;
import com.ruangong.heritage.enums.UserType;
import com.ruangong.heritage.exception.BusinessException;
import com.ruangong.heritage.exception.ServiceException;
import com.ruangong.heritage.mapper.UserMapper;
import com.ruangong.heritage.service.convert.UserConvert;
import com.ruangong.heritage.util.JwtTokenUtils;
import jakarta.annotation.Resource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class UserService {

    @Resource
    UserMapper userMapper;

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

    @Transactional(rollbackFor = Exception.class)
    public UserDetailResponseDTO register(UserRegisterCommandDTO registerDTO) {

        // 校验信息
        /**
         *  1、两次密码不一致
         *  2、用户名已经存在
         *  3、邮箱被注册了
         *  4、无效的用户类型
         */
        try {
            if (!registerDTO.getPassword().equals(registerDTO.getConfirmPassword())) {
                throw new BusinessException("两次密码不一致");
            }
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("username", registerDTO.getUsername());
            int count = userMapper.selectCount(queryWrapper).intValue();
            if (count > 0) {
                throw new BusinessException("用户名已经存在");
            }

            QueryWrapper<User> queryWrapper2 = new QueryWrapper<>();
            queryWrapper2.eq("email", registerDTO.getEmail());
            int count2 = userMapper.selectCount(queryWrapper2).intValue();
            if (count2 > 0) {
                throw new BusinessException("邮箱已经存在");
            }

            // 类型校验
            if (!UserType.isValidCode(registerDTO.getUserType())) {
                throw new BusinessException("用户类型无效");
            }


            String encodePassword = passwordEncoder.encode(registerDTO.getPassword());
            // 我们只有一个dto 对象，没有user 对象，所以需要编写一个方法转换一下
            User user = UserConvert.registerCommandToEntry(registerDTO, encodePassword);
            userMapper.insert(user);

            // 注册成功后，返回一个实体
            UserDetailResponseDTO responseDTO = UserConvert.entryToDetailResponse(user);

            return responseDTO;


        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("注册失败，请重试");
        }


    }

    public UserDetailResponseDTO getUserById(Long userId) {

        User user = userMapper.selectById(userId);
        UserDetailResponseDTO responseDTO = UserConvert.entryToDetailResponse(user);
        return responseDTO;
    }

    @Transactional(rollbackFor = Exception.class)
    public UserLoginResponseDTO login(UserLoginCommandDTO userLoginCommandDTO) {

        // 根据账号和密码获取数据
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, userLoginCommandDTO.getUsername())
                .or()
                .eq(User::getEmail, userLoginCommandDTO.getUsername());

        User user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        if (!passwordEncoder.matches(userLoginCommandDTO.getPassword(), user.getPassword())) {
            throw new BusinessException("用户名或者密码错误");
        }
        if (!user.isActive()) {
            throw new BusinessException("用户已禁用，请联系管理员");
        }

        String token = JwtTokenUtils.generateToken(user.getId(), user.getUsername(), user.getUserType());

        return UserConvert.buildLoginResponse(user, token);

    }

    @Transactional(rollbackFor = Exception.class)
    public void resetPassword(UserResetPasswordCommandDTO userResetPasswordCommandDTO) {

        try {
            // 判断的是用户名是否存在
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getUsername, userResetPasswordCommandDTO.getUsername());
            User user = userMapper.selectOne(queryWrapper);
            if (user == null) {
                throw new BusinessException("用户不存在");
            }
            // 校验邮箱是否正确
            if (!user.getEmail().equals(userResetPasswordCommandDTO.getEmail())) {
                throw new BusinessException("邮箱和用户名不匹配");
            }
            // 校验手机号是否正确
            if (!user.getPhone().equals(userResetPasswordCommandDTO.getPhone())) {
                throw new BusinessException("手机号和用户名不匹配");
            }
            // 重置密码开始
            user.setPassword(passwordEncoder.encode(userResetPasswordCommandDTO.getNewPassword()));
            user.setUpdatedAt(LocalDateTime.now());
            userMapper.updateById(user);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("密码重置失败，请稍后再试");
        }


    }

    @Transactional(rollbackFor = Exception.class)
    public Result<UserDetailResponseDTO> getCurrentUser() {
        //如何获取当前用户呢？
        try {
            UserDetailResponseDTO currentUser = JwtTokenUtils.getCurrentUser();
            if (currentUser == null) {
                return Result.error("未登录或者token已过期");
            }
            return Result.success(currentUser);
        } catch (Exception e) {

            return Result.error("获取用户失败！");

        }
    }

    // 更新用户信息
    @Transactional(rollbackFor = Exception.class)
    public UserDetailResponseDTO updateUser(Long userId, UserUpdateCommandDTO updateDto) {

        try {
            User user = userMapper.selectById(userId);
            if (user == null) {
                throw new BusinessException("该用户不存在");
            }
            // 你编写的邮箱是否已经被其他人使用
            if (updateDto.getEmail() != null && !updateDto.getEmail().equals(user.getEmail())) {
                QueryWrapper<User> queryWrapper = new QueryWrapper<>();
                queryWrapper.ne("id", user.getId());
                queryWrapper.eq("email", updateDto.getEmail());
                int i = userMapper.selectCount(queryWrapper).intValue();
                if (i > 0) {
                    throw new BusinessException("邮箱已经被其他人使用");
                }
            }
            // 验证用户类型和状态的有效性
            if (updateDto.getUserType() != null && !UserType.isValidCode(updateDto.getUserType())) {
                throw new BusinessException("无效的用户类型");
            }

            // 将页面传递过来的数据updateDto 放入到 user 类中
            UserConvert.applyUpdateToEntity(user, updateDto);
            userMapper.updateById(user);

            return UserConvert.entryToDetailResponse(user);
        }catch (BusinessException e){
            throw e;
        }catch (Exception e){
            throw new ServiceException("更新用户信息失败");
        }
    }

    public void updatePassword(Long userId, UserPasswordUpdateCommandDTO passwordUpdateCommandDTO) {
        try {
            User user = userMapper.selectById(userId);
            if (user == null) {
                throw new BusinessException("该用户不存在");
            }
            // 验证老密码是否正确
            if(!passwordEncoder.matches(passwordUpdateCommandDTO.getOldPassword(),user.getPassword())){
                throw new BusinessException("旧密码不正确");
            }
            String newEncodePassword = passwordEncoder.encode(passwordUpdateCommandDTO.getNewPassword());
            user.setPassword(newEncodePassword);
            user.setUpdatedAt(LocalDateTime.now());
            userMapper.updateById(user);

        }catch (BusinessException e){
            throw e;
        }catch (Exception e){
            throw new ServiceException("更新用户信息失败");
        }

    }
}
