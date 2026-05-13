package com.ruangong.heritage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * AI聊天消息实体类
 * @author system
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("ai_chat_message")
//AI聊天消息实体类
public class AiChatMessage {

    @TableId(type = IdType.AUTO)
    //消息ID
    private Long id;

    //会话ID
    private String sessionId;

    //角色：user-用户，assistant-AI助手
    private String role;

    //消息内容
    private String content;


    private LocalDateTime createTime;
}
