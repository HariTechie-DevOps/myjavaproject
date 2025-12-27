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
            // This line actually takes the data and puts it into MySQL
            userRepository.save(user);
            
            response.put("success", true);
            response.put("token", "session_token_12345");
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Mobile number might already exist!");
            response.put("field", "mobile");
        }
        return response;
    }
}
