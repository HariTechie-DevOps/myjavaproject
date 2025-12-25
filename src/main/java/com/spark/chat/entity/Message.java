package com.spark.chat.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
@Getter 
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Long chatRoomId;
    private Long senderId;
    
    @Column(columnDefinition = "TEXT") // Allows for very long chat messages
    private String originalText;
    
    private String originalLanguage;
    private LocalDateTime timestamp;
}
