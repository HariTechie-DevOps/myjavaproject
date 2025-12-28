package com.example.signup.entity;

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

    @Column(unique = true, nullable = false)
    private String mobile;

    @Column(nullable = false) // REMOVE unique = true here
    private String password;

    private String token;
}
