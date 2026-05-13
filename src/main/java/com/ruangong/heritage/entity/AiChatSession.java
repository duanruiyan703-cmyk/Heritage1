package com.ruangong.heritage.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * AI聊天会话实体类
 * @author system
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("ai_chat_session")
//AI聊天会话实体类
public class AiChatSession {

    @TableId(type = IdType.AUTO)
    //会话ID
    private Long id;

    //会话唯一标识(UUID)
    @TableField("session_id")
    private String sessionId;

    //"用户ID"
    @TableField("user_id")
    private Long userId;

    //会话标题
    private String title;

    //创建时间
    private LocalDateTime createTime;


    private LocalDateTime updateTime;
}
