package com.ruangong.heritage.service;


import com.ruangong.heritage.ai.PromptManage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Slf4j
@Service
@RequiredArgsConstructor
public class HeritageAssistantService {

    @Qualifier("open-ai")
    private final ChatClient chatClient;
    @Autowired
    private AiChatSessionService sessionService;

    /**
     * 流式对话（带会话记忆）
     *
     * @param sessionId 会话ID
     * @param userMessage 用户消息
     * @return Flux流式响应
     */
    public Flux<String> chatStream(String sessionId, String userMessage) {
        log.info("开始AI对话，sessionId: {}, userMessage: {}", sessionId, userMessage);

        // 保存用户消息
        sessionService.saveMessage(sessionId, "user", userMessage);

        // 构建流式响应
        Flux<String> responseFlux = chatClient.prompt()
                .system(PromptManage.HERITAGE_ASSISTANT_PROMPT)
                .user(userMessage)
                .advisors(advisorSpec -> advisorSpec
                        .param(ChatMemory.CONVERSATION_ID, sessionId))
                .stream()
                .content();

        // 收集完整响应并保存
        StringBuilder fullResponse = new StringBuilder();
        return responseFlux
                .doOnNext(fullResponse::append)
                .doOnComplete(() -> {
                    String assistantMessage = fullResponse.toString();
                    sessionService.saveMessage(sessionId, "assistant", assistantMessage);
                    log.info("AI对话完成，sessionId: {}, 响应长度: {}", sessionId, assistantMessage.length());
                })
                .doOnError(error -> {
                    log.error("AI对话失败，sessionId: {}", sessionId, error);
                });
    }
}

