package com.spark.chat.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SignupController {
    
    @GetMapping("/signup")
    public String signupPage() {
        // This looks for signup.html in src/main/resources/static or templates
        return "signup.html"; 
    }
}
