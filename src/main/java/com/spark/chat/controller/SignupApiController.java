package com.spark.chat.controller;


import com.spark.chat.dto.SignupRequest;
import com.spark.chat.dto.SignupResponse;
import com.spark.chat.service.SignupService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class SignupApiController {

    private final SignupService signupService;

    public SignupApiController(SignupService signupService) {
        this.signupService = signupService;
    }

    @PostMapping("/signup")
    public SignupResponse processSignup(@RequestBody SignupRequest request) {
        // Calls the service to check uniqueness and save to DB
        return signupService.signup(request);
    }
}
