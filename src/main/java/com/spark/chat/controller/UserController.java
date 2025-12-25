package com.spark.chat.controller;

import com.spark.chat.dto.UserLanguageRequest;
import com.spark.chat.entity.User;
import com.spark.chat.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping("/language")
    public ResponseEntity<User> setLanguage(@RequestBody UserLanguageRequest req) {
        User updatedUser = service.setLanguage(req.getUserId(), req.getLanguage());
        // Note: In real production, return a UserResponseDTO here to hide password fields
        return ResponseEntity.ok(updatedUser);
    }
}
