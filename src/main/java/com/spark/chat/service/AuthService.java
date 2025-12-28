package com.example.signup.service;

import com.example.signup.dto.*;
import com.example.signup.entity.User;
import com.example.signup.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value; // Crucial Import
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

    // 1. SECURE KEY: Spring pulls this from application.properties or Env Variables
    @Value("${fast2sms.api.key}")
    private String apiKey;

    private final UserRepository repo;

    public AuthService(UserRepository repo) {
        this.repo = repo;
    }

    public SignupResponse registerUser(SignupRequest req) {
    // Check if user already exists
    if (repo.findByMobile(req.mobile).isPresent()) {
        return new SignupResponse(false, "mobile", "Mobile already registered", null, null, 0, null);
    }
    
    // Create and save new user
    User newUser = new User();
    newUser.setName(req.name);
    newUser.setMobile(req.mobile);
    newUser.setPassword(req.password);
    newUser.setAge(req.age);
    newUser.setGender(req.gender);
    
    repo.save(newUser);
    return new SignupResponse(true, null, "User registered successfully", null, req.name, req.age, req.gender);
}

    // --- SIGN IN LOGIC ---
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

    // --- FORGOT PASSWORD: STEP 1 (Generate, Save, and Send) ---
    public boolean generateOtp(String mobile) {
        Optional<User> userOpt = repo.findByMobile(mobile);
        if (userOpt.isPresent()) {
            String otp = String.format("%06d", new Random().nextInt(999999));

            User user = userOpt.get();
            user.setOtp(otp);
            repo.save(user);

            try {
                String cleanMobile = mobile.replace("+91", "").replace("+", "").trim();
                
                // 2. USING THE SECURE VARIABLE: Notice we use 'apiKey' here
                String urlString = "https://www.fast2sms.com/dev/bulkV2?authorization=" + apiKey + 
                                   "&route=otp&variables_values=" + otp + 
                                   "&numbers=" + cleanMobile;

                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("cache-control", "no-cache");
                conn.setRequestProperty("User-Agent", "Mozilla/5.0");

                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = in.readLine()) != null) {
                        response.append(line);
                    }
                    in.close();

                    if (response.toString().contains("true")) {
                        System.out.println("FAST2SMS SUCCESS: OTP " + otp + " sent to " + cleanMobile);
                        return true;
                    }
                }
                System.err.println("FAST2SMS REJECTION: HTTP Code " + responseCode);
                return false;
            } catch (Exception e) {
                System.err.println("CONNECTION ERROR: " + e.getMessage());
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
