package com.spark.chat.controller;

import com.spark.chat.dto.*;
import com.spark.chat.service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chat")
public class ChatController {
    private final ChatService service;

    public ChatController(ChatService service) {
        this.service = service;
    }

    @PostMapping("/send")
    public ResponseEntity<MessageResponse> send(@RequestBody MessageRequest req) {
        // Real-world practice: receiverId and senderId are usually part of the RequestBody 
        // or extracted from a JWT token.
        MessageResponse response = service.sendMessage(
            req.getChatRoomId(), 
            req.getSenderId(), 
            req.getReceiverId(), 
            req.getMessage()
        );
        return ResponseEntity.ok(response);
    }
}
