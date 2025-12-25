package com.spark.chat.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter 
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserLanguageRequest {
    private Long userId;
    private String language; // Example: "en", "es", "fr"
}
