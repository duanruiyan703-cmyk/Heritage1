package com.ruangong.heritage.controller;


import com.ruangong.heritage.common.Result;
import com.ruangong.heritage.dto.command.AiChatCommandDTO;
import com.ruangong.heritage.service.AiChatSessionService;
import com.ruangong.heritage.service.HeritageAssistantService;
import com.ruangong.heritage.util.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@Slf4j
@RestController
@RequestMapping("/ai-chat")
@RequiredArgsConstructor
public class AiChatController {

    private final HeritageAssistantService aiService;

    /*@Autowired
    AiChatService aiChatService;*/


    private final AiChatSessionService sessionService;

    @PostMapping("/session/start")
    public Result<String> creeateNewSession(String title){


        Long userId = JwtTokenUtils.getCurrentUserId();
        String sessionId =sessionService.createSession(userId,title);

        return Result.success(sessionId);
    }

    @PostMapping(value="/stream",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> chatStream(@RequestBody AiChatCommandDTO dto){
        Long userId = JwtTokenUtils.getCurrentUserId();

        // 验证权限
       /* if (!sessionService.isSessionOwnedByUser(dto.getSessionId(), userId)) {
            return Flux.just("data: 无权访问此会话\n\n");
        }*/

        log.info("开始流式对话，sessionId: {}, userId: {}", dto.getSessionId(), userId);

        // 返回 Flux，Spring WebFlux 会自动处理 SSE 流
        // Spring 会自动为每个元素添加 "data: " 前缀和 "\n\n" 后缀
        // 所以我们只需要返回内容本身即可

        return aiService.chatStream(dto.getSessionId(),dto.getUserMessage())
                .concatWith(Flux.just("[DONE]"))
                .doOnError(error -> {
                    log.error("AI对话流失败", error);
                }).onErrorResume(error ->
                        Flux.just("[ERROR]" + error.getMessage())
                );

    }
}
