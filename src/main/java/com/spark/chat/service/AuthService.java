package com.example.signup.service;

import com.example.signup.dto.*;
import com.example.signup.entity.User;
import com.example.signup.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
public class AuthService {

    // PASTE YOUR KEY HERE
    private static final String API_KEY = "YOUR_FAST2SMS_API_KEY_HERE";

    private final UserRepository repo;

    public AuthService(UserRepository repo) {
        this.repo = repo;
    }

    // --- SIGN IN ---
    public SignupResponse authenticate(SigninRequest req) {
        Optional<User> userOpt = repo.findByMobile(req.mobile);
        if (userOpt.isEmpty()) {
            return new SignupResponse(false, "mobile", "Account does not exist.", null, null, 0, null);
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

    // --- FORGOT PASSWORD: STEP 1 (Send OTP via Fast2SMS) ---
    public boolean generateOtp(String mobile) {
        Optional<User> userOpt = repo.findByMobile(mobile);
        if (userOpt.isPresent()) {
            String otp = String.format("%06d", new Random().nextInt(999999));
            User user = userOpt.get();
            user.setOtp(otp); 
            repo.save(user);

            // REAL-TIME SEND VIA FAST2SMS
            try {
                // Remove "+" if present (Fast2SMS needs 10 digits)
                String cleanMobile = mobile.replace("+91", "").replace("+", "").trim();
                
                String urlString = "https://www.fast2sms.com/dev/bulkV2?authorization=" + API_KEY + 
                                   "&route=otp&variables_values=" + otp + 
                                   "&numbers=" + cleanMobile;

                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                int responseCode = conn.getResponseCode();
                if (responseCode == 200) {
                    System.out.println("OTP Sent Successfully to " + cleanMobile);
                    return true;
                } else {
                    System.out.println("Fast2SMS Error Code: " + responseCode);
                    return false;
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                return false;
            }
        }
        return false;
    }

    public boolean verifyOtp(String mobile, String otp) {
        return repo.findByMobile(mobile)
                .map(user -> user.getOtp() != null && user.getOtp().equals(otp))
                .orElse(false);
    }

    public void updatePassword(String mobile, String newPassword) {
        repo.updatePassword(mobile, newPassword);
    }
}
