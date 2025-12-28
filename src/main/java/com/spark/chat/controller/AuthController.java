package com.spark.chat.controller;

import com.example.signup.dto.*;
import com.spark.chat.service.AuthService; // CHANGE THIS LINE
import com.example.signup.repository.UserRepository;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AuthController {

    private final AuthService authService;
    private final UserRepository repo; // Added this variable

    // Updated Constructor to include the repo
    public AuthController(AuthService authService, UserRepository repo) {
        this.authService = authService;
        this.repo = repo;
    }

   // --- STEP 1: CREATE ACCOUNT ---
    // This is called by your signup.html page
    @PostMapping("/signup")
    public SignupResponse signup(@RequestBody SignupRequest req) {
        return authService.registerUser(req); 
    }

    // --- STEP 2: LOGIN TO ACCOUNT ---
    // This is called by your signin.html page
    @PostMapping("/signin")
    public SignupResponse signin(@RequestBody SigninRequest req) {
        return authService.authenticate(req);
    }

    @PostMapping("/forgot-password/send-otp")
    public Map<String, Object> sendOtp(@RequestBody Map<String, String> body) {
        boolean success = authService.generateOtp(body.get("mobile"));
        return Map.of("success", success, "message", success ? "OTP Sent" : "Mobile not found");
    }

    @PostMapping("/forgot-password/verify-otp")
    public Map<String, Object> verify(@RequestBody Map<String, String> body) {
        boolean success = authService.verifyOtp(body.get("mobile"), body.get("otp"));
        return Map.of("success", success, "message", success ? "Verified" : "Invalid OTP");
    }

    @PostMapping("/forgot-password/update")
    public Map<String, Object> update(@RequestBody ForgotPasswordRequest req) {
        authService.updatePassword(req.mobile, req.newPassword);
        return Map.of("success", true);
    }

    // This method was causing the error because 'repo' was missing
    @GetMapping("/user/profile")
    public SignupResponse getProfile(@RequestParam String token) {
        return repo.findAll().stream()
            .filter(u -> token.equals(u.getToken()))
            .findFirst()
            .map(u -> new SignupResponse(true, null, "Found", u.getToken(), u.getName(), u.getAge(), u.getGender()))
            .orElseThrow(() -> new RuntimeException("Invalid Token"));
    }
}
