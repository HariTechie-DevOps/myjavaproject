package com.spark.chat.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users") // 'User' is often a reserved word in many databases
@Getter 
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String username;
    private String preferredLanguage; // e.g., "en", "es"
}
