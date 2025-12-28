package com.spark.chat.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int age;
    private String gender;

    @Column(unique = true, nullable = false, length = 15)
    private String mobile;

    @Column(nullable = false)
    private String password;

    private String token;

    // Added for Forgot Password logic
    private String otp; 
}
