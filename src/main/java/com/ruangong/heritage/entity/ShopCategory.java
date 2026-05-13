package com.ruangong.heritage.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 商品分类实体类
 * @author system
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
//shop_category
//商品分类实体类
public class ShopCategory {


    private Long id;


    private String name;


    private Integer status;


    private LocalDateTime createTime;


    private LocalDateTime updateTime;


    /**
     * 是否启用
     */
    public boolean isEnabled() {
        return this.status != null && this.status == 1;
    }

    /**
     * 是否禁用
     */
    public boolean isDisabled() {
        return this.status != null && this.status == 0;
    }
}

