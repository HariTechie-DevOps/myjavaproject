package com.example.signup.service;

import com.example.signup.dto.*;
import com.example.signup.entity.User;
import com.example.signup.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
public class AuthService {
    private final UserRepository repo;

    public AuthService(UserRepository repo) {
        this.repo = repo;
    }

    // SIGN IN LOGIC
    public SignupResponse authenticate(SigninRequest req) {
        Optional<User> userOpt = repo.findByMobile(req.mobile);
        
        if (userOpt.isEmpty()) {
            return new SignupResponse(false, "mobile", "Account does not exist. Please Sign Up.", null, null, 0, null);
        }

        User user = userOpt.get();
        if (!user.getPassword().equals(req.password)) {
            return new SignupResponse(false, "password", "Incorrect password", null, null, 0, null);
        }

        // Generate a new session token on every login
        String token = UUID.randomUUID().toString();
        user.setToken(token);
        repo.save(user);

        return new SignupResponse(true, null, "Login successful", token, user.getName(), user.getAge(), user.getGender());
    }

    // FORGOT PASSWORD: STEP 1 (Send OTP)
    public boolean generateOtp(String mobile) {
        Optional<User> userOpt = repo.findByMobile(mobile);
        if (userOpt.isPresent()) {
            // 1. Generate 6-digit code
            String otp = String.format("%06d", new Random().nextInt(999999));
        
            // 2. Save to DB (This ensures the 'Check' works later)
            User user = userOpt.get();
            user.setOtp(otp); 
            repo.save(user);

            // 3. REAL-WORLD SEND (Example using Twilio)
            // Message.creator(new PhoneNumber(mobile), new PhoneNumber(FROM), "Your code is: " + otp).create();
        
            System.out.println("DEBUG: OTP " + otp + " sent to physical phone: " + mobile);
            return true;
        }
        return false;
    }

    // FORGOT PASSWORD: STEP 2 (Verify)
    public boolean verifyOtp(String mobile, String otp) {
        return repo.findByMobile(mobile)
                .map(user -> user.getOtp() != null && user.getOtp().equals(otp))
                .orElse(false);
    }

    // FORGOT PASSWORD: STEP 3 (Update)
    public void updatePassword(String mobile, String newPassword) {
        repo.updatePassword(mobile, newPassword);
    }
}
