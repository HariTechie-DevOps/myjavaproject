package com.spark.chat.service;

import com.spark.chat.dto.MessageResponse;
import com.spark.chat.entity.*;
import com.spark.chat.repository.MessageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
public class ChatService {
    private final MessageRepository msgRepo;
    private final UserService userService;
    private final TranslationService translator;

    public ChatService(MessageRepository m, UserService u, TranslationService t) {
        this.msgRepo = m;
        this.userService = u;
        this.translator = t;
    }

    @Transactional
    public MessageResponse sendMessage(Long chatRoomId, Long senderId, Long receiverId, String text) {
        // 1. Fetch Users (Assuming these throw exceptions if not found)
        User sender = userService.get(senderId);
        User receiver = userService.get(receiverId);

        // 2. Create and Populate Message Entity
        Message msg = new Message();
        msg.setChatRoomId(chatRoomId);
        msg.setSenderId(senderId);
        msg.setOriginalText(text);
        msg.setOriginalLanguage(sender.getPreferredLanguage());
        msg.setTimestamp(LocalDateTime.now());

        // 3. Save to DB (The returned 'savedMsg' will have the generated ID)
        Message savedMsg = msgRepo.save(msg);

        // 4. Handle Translation Logic
        boolean needsTranslation = !sender.getPreferredLanguage().equals(receiver.getPreferredLanguage());
        String translatedText = null;

        if (needsTranslation) {
            // Example: Sending "Hello" (English) to a Spanish user returns "Hola"
            translatedText = translator.translate(text, sender.getPreferredLanguage(), receiver.getPreferredLanguage());
        }

        // 5. Return Response using the ID from the saved entity
        return new MessageResponse(
            savedMsg.getId(), 
            text, 
            translatedText, 
            needsTranslation
        );
    }
}
