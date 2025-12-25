package com.spark.chat.dto;

import lombok.*;

@Getter 
@Setter 
@AllArgsConstructor
@NoArgsConstructor // Required for JSON serialization/deserialization
public class MessageResponse {
    private Long messageId;
    private String originalText;
    private String translatedText;
    private boolean showTranslation;
}
