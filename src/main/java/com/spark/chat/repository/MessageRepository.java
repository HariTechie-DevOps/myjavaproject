package com.spark.chat.repository;

import com.spark.chat.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    
    // Used when a user joins a chat room to see the message history (like Slack)
    List<Message> findByChatRoomId(Long chatRoomId);
}
