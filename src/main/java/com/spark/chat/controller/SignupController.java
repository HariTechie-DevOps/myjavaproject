package com.example.signup.controller;

import org.springframework.web.bind.annotation.*;

import com.example.signup.dto.SignupRequest;
import com.example.signup.dto.SignupResponse;
import com.example.signup.service.SignupService;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class SignupController {

    private final SignupService service;

    public SignupController(SignupService service) {
        this.service = service;
    }

    @PostMapping("/signup")
    public SignupResponse signup(@RequestBody SignupRequest request) {
        return service.signup(request);
    }
}
