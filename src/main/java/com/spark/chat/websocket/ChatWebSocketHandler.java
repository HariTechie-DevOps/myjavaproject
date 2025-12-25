package com.spark.chat.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spark.chat.dto.MessageResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {
    
    // Stores active sessions by User ID (Real-world: use a distributed cache like Redis for scaling)
    private final ConcurrentHashMap<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // Example: /ws/chat?userId=1
        String query = session.getUri().getQuery();
        String userId = query.split("=")[1];
        sessions.put(userId, session);
    }

    public void sendMessageToUser(Long userId, MessageResponse message) throws Exception {
        WebSocketSession session = sessions.get(String.valueOf(userId));
        if (session != null && session.isOpen()) {
            String jsonMessage = objectMapper.writeValueAsString(message);
            session.sendMessage(new TextMessage(jsonMessage));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.values().remove(session);
    }
}
