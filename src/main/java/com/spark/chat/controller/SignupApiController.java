package com.example.signup.controller;

import com.example.signup.dto.SignupRequest;
import com.example.signup.dto.SignupResponse;
import com.example.signup.service.SignupService;
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
