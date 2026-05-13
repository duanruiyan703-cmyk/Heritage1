package com.ruangong.heritage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder // 通过构建者模式创建一个类
public class User {

    @TableId(type = IdType.AUTO)
    private Long id;


    private String username;


    private String password;


    private String email;


    private String phone;


    private String userType;


    private String name;


    private String avatar;


    private Integer status;


    private LocalDateTime createdAt;


    private LocalDateTime updatedAt;


    private String sex;


    /**
     * 是否为管理员
     */
    public boolean isAdmin() {
        return "ADMIN".equals(this.userType);
    }

    /**
     * 是否为普通用户
     */
    public boolean isUser() {
        return "USER".equals(this.userType);
    }

    /**
     * 是否为正常状态
     */
    public boolean isActive() {
        return this.status != null && this.status == 1;
    }

    /**
     * 是否被禁用
     */
    public boolean isDisabled() {
        return this.status != null && this.status == 0;
    }

    /**
     * 获取用户类型显示名称
     */
    public String getUserTypeDisplayName() {
        if (userType == null) {
            return "未知";
        }
        switch (userType) {
            case "ADMIN":
                return "管理员";
            case "USER":
                return "普通用户";
            default:
                return "未知";
        }
    }

    /**
     * 获取用户状态显示名称
     */
    public String getStatusDisplayName() {
        if (status == null) {
            return "未知";
        }
        switch (status) {
            case 1:
                return "正常";
            case 0:
                return "禁用";
            default:
                return "未知";
        }
    }

    /**
     * 获取显示名称（优先使用姓名，其次用户名）
     */
    public String getDisplayName() {
        if (name != null && !name.trim().isEmpty()) {
            return name;
        }
        return username;
    }
}
