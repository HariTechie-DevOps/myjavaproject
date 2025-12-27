package com.spark.chat.controller;

import com.example.signup.entity.User;
import com.example.signup.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") // Allows your browser to talk to the backend without security blocks
public class SignupApiController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/signup")
    public Map<String, Object> registerUser(@RequestBody User user) {
        Map<String, Object> response = new HashMap<>();
        try {
            User savedUser = userRepository.save(user);
            response.put("success", true);
            response.put("token", "session_token_12345");
        
        // Adding age and gender to the response
            response.put("userName", savedUser.getName());
            response.put("userAge", savedUser.getAge());
            response.put("userGender", savedUser.getGender());
        
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Registration failed!");
        }
        return response;
    }
}
