package com.ruangong.heritage.service;

import com.ruangong.heritage.entity.AiChatMessage;
import com.ruangong.heritage.entity.AiChatSession;
import com.ruangong.heritage.mapper.AiChatMessageMapper;
import com.ruangong.heritage.mapper.AiChatSessionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AiChatSessionService {

    @Autowired
    AiChatSessionMapper aiChatSessionMapper;

    @Autowired
    AiChatMessageMapper aiChatMessageMapper;

    public String createSession(Long userId, String title) {

        String sessionId = UUID.randomUUID().toString();
        AiChatSession aiChatSession = new AiChatSession();
        aiChatSession.setSessionId(sessionId);
        aiChatSession.setTitle(title==null?"新会话":title);
        aiChatSession.setUserId(userId);
        aiChatSession.setCreateTime(LocalDateTime.now());
        aiChatSessionMapper.insert(aiChatSession);
        return sessionId;
    }

  /*  public boolean isSessionOwnedByUser(String sessionId, Long userId) {
        AiChatSession session = getSessionById(sessionId);
        return session != null && session.getUserId().equals(userId);
    }
*/



    public void saveMessage(String sessionId, String role, String userMessage) {
        AiChatMessage aiChatMessage = new AiChatMessage();
        aiChatMessage.setSessionId(sessionId);
        aiChatMessage.setRole(role);
        aiChatMessage.setCreateTime(LocalDateTime.now());
        aiChatMessage.setContent(userMessage);

        aiChatMessageMapper.insert(aiChatMessage);
    }


}
