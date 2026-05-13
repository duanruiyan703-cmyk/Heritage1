package com.ruangong.heritage.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;

import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ChatClient 配置类
 * 
 * @author system
 */
@Configuration
public class ChatClientConfig {
    public static final Integer MAX_MEMORY_MESSAGE_SIZE = 30;
    
    /**
     * 配置 ChatMemory - 内存存储的会话记忆
     * 
     * @return ChatMemory 内存存储的会话记忆实例
     */
    @Bean
    public ChatMemory chatMemory() {
        return MessageWindowChatMemory.builder()
                .maxMessages(MAX_MEMORY_MESSAGE_SIZE) // 窗口最大消息数目，保留最近30条消息
                .build();
    }


    /**
     * 配置 ChatClient - OpenAI兼容的聊天客户端
     * 
     * Spring AI 会自动扫描带有 @Tool 注解的方法，无需手动注册
     * 
     * @param openAiChatModel OpenAI Chat模型（由Spring AI自动配置）
     * @param chatMemory 会话记忆存储
     * @return ChatClient 实例
     */
    @Bean("open-ai")
    public ChatClient openAIChatClient(
            OpenAiChatModel openAiChatModel, 
            ChatMemory chatMemory) {
        return ChatClient.builder(openAiChatModel)
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory()).build())
                .build();
    }
}

