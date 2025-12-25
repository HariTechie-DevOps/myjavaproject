package com.spark.chat.dto;
import lombok.Getter; import lombok.Setter;

@Getter @Setter
public class MessageRequest {
    private Long chatRoomId;
    private Long senderId;
    private Long receiverId; // Added this to match your Service logic
    private String message;
}
