package com.spark.chat.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "chat_rooms")
@Getter 
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoom {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Long user1Id;
    private Long user2Id;
}
