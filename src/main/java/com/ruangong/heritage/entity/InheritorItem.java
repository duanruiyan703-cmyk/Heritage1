package com.ruangong.heritage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 传承人与作品关联实体类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("inheritor_item")
//"传承人与作品关联实体类
public class InheritorItem {

    @TableId(type = IdType.AUTO)
    private Long id;


    private String inheritorId;


    private String itemId;
}


