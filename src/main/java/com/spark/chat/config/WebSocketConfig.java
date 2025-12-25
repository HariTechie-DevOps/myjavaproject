package com.spark.chat.config;

import com.spark.chat.websocket.ChatWebSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final ChatWebSocketHandler handler;

    public WebSocketConfig(ChatWebSocketHandler handler) {
        this.handler = handler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // Mapping the handler to the /ws/chat endpoint
        registry.addHandler(handler, "/ws/chat")
                .setAllowedOrigins("*") // In production, replace "*" with "http://yourdomain.com"
                .withSockJS(); // Real-world: Adds a fallback for browsers that don't support WebSockets
    }
}
