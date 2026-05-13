package com.ruangong.heritage.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品实体类
 * @author system
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
//shop_product
//"商品实体类"
public class ShopProduct {


    private String id;


    private String title;


    private String subtitle;


    private Long categoryId;


    private BigDecimal price;


    private Integer stock;


    private String detail;


    private Integer status;


    private LocalDateTime createTime;


    private LocalDateTime updateTime;

    /**
     * 是否上架
     */
    public boolean isOnSale() {
        return this.status != null && this.status == 1;
    }

    /**
     * 是否下架
     */
    public boolean isOffShelf() {
        return this.status != null && this.status == 0;
    }

    /**
     * 是否有库存
     */
    public boolean hasStock() {
        return this.stock != null && this.stock > 0;
    }

    /**
     * 是否可以购买
     */
    public boolean canPurchase() {
        return isOnSale() && hasStock();
    }

    /**
     * 获取状态显示名称
     */
    public String getStatusDisplayName() {
        if (status == null) {
            return "未知";
        }
        switch (status) {
            case 0:
                return "下架";
            case 1:
                return "上架";
            default:
                return "未知";
        }
    }

    /**
     * 获取业务标识（直接返回ID）
     */
    public String getBusinessId() {
        return id;
    }

    /**
     * 是否使用UUID格式的ID
     */
    public boolean isUsingUuid() {
        if (id == null || id.trim().isEmpty()) {
            return false;
        }
        // 简单的UUID格式检查：包含4个连字符的36字符字符串
        return id.length() == 36 && id.chars().filter(ch -> ch == '-').count() == 4;
    }

    /**
     * 是否使用数字格式的ID
     */
    public boolean isUsingNumericId() {
        if (id == null || id.trim().isEmpty()) {
            return false;
        }
        try {
            Long.parseLong(id);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * 减少库存
     * @param quantity 减少数量
     * @return 是否成功
     */
    public boolean reduceStock(int quantity) {
        if (this.stock == null || this.stock < quantity) {
            return false;
        }
        this.stock -= quantity;
        return true;
    }

    /**
     * 增加库存
     * @param quantity 增加数量
     */
    public void addStock(int quantity) {
        if (this.stock == null) {
            this.stock = 0;
        }
        this.stock += quantity;
    }

    /**
     * 检查库存是否足够
     * @param quantity 需要的数量
     * @return 是否足够
     */
    public boolean checkStock(int quantity) {
        return this.stock != null && this.stock >= quantity;
    }
}


