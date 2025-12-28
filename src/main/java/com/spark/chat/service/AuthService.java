package com.example.signup.service;

import com.example.signup.dto.*;
import com.example.signup.entity.User;
import com.example.signup.repository.UserRepository;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
public class AuthService {
    
    // REPLACE THESE with your real keys from Twilio.com console
    public static final String ACCOUNT_SID = "ACyour_real_sid_here"; 
    public static final String AUTH_TOKEN = "your_real_auth_token_here";
    public static final String TWILIO_PHONE = "+1234567890"; // Your Twilio number

    private final UserRepository repo;

    public AuthService(UserRepository repo) {
        this.repo = repo;
    }

    // --- SIGN IN LOGIC ---
    public SignupResponse authenticate(SigninRequest req) {
        Optional<User> userOpt = repo.findByMobile(req.mobile);
        if (userOpt.isEmpty()) {
            return new SignupResponse(false, "mobile", "Account does not exist. Please Sign Up.", null, null, 0, null);
        }
        User user = userOpt.get();
        if (!user.getPassword().equals(req.password)) {
            return new SignupResponse(false, "password", "Incorrect password", null, null, 0, null);
        }
        String token = UUID.randomUUID().toString();
        user.setToken(token);
        repo.save(user);
        return new SignupResponse(true, null, "Login successful", token, user.getName(), user.getAge(), user.getGender());
    }

    // --- FORGOT PASSWORD: STEP 1 (Send OTP to REAL PHONE) ---
    public boolean generateOtp(String mobile) {
        Optional<User> userOpt = repo.findByMobile(mobile);
        if (userOpt.isPresent()) {
            // 1. Generate 6-digit code
            String otp = String.format("%06d", new Random().nextInt(999999));
        
            // 2. Save to DB so verifyOtp can find it later
            User user = userOpt.get();
            user.setOtp(otp); 
            repo.save(user);

            // 3. SEND TO REAL PHONE VIA TWILIO
            try {
                Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
                Message.creator(
                    new PhoneNumber(mobile), // Target mobile (e.g. +91...)
                    new PhoneNumber(TWILIO_PHONE), 
                    "Your Real-Time App OTP is: " + otp
                ).create();
                
                System.out.println("OTP successfully sent to real phone: " + mobile);
                return true;
            } catch (Exception e) {
                System.err.println("Twilio SMS failed: " + e.getMessage());
                // For local testing if Twilio fails
                System.out.println("FALLBACK: View OTP in Console: " + otp);
                return false;
            }
        }
        return false;
    }

    // --- FORGOT PASSWORD: STEP 2 (Verify) ---
    public boolean verifyOtp(String mobile, String otp) {
        return repo.findByMobile(mobile)
                .map(user -> user.getOtp() != null && user.getOtp().equals(otp))
                .orElse(false);
    }

    // --- FORGOT PASSWORD: STEP 3 (Update) ---
    public void updatePassword(String mobile, String newPassword) {
        repo.updatePassword(mobile, newPassword);
    }
}
