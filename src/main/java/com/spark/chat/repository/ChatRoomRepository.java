package com.spark.chat.repository;

import com.spark.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    // Real-world use case: Find a room between two specific users
    // ChatRoom findByUser1IdAndUser2Id(Long user1, Long user2);
}
