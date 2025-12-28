package com.spark.chat.service; // Check this carefully!

import com.example.signup.dto.*;
import com.example.signup.entity.User;
import com.example.signup.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${fast2sms.api.key}")
    private String apiKey;

    private final UserRepository repo;

    public AuthService(UserRepository repo) {
        this.repo = repo;
    }

    // 1. REGISTER NEW USER
    public SignupResponse registerUser(SignupRequest req) {
        if (repo.findByMobile(req.mobile).isPresent()) {
            return new SignupResponse(false, "mobile", "Mobile already registered", null, null, 0, null);
        }
        User newUser = new User();
        newUser.setName(req.name);
        newUser.setMobile(req.mobile);
        newUser.setPassword(req.password);
        newUser.setAge(req.age);
        newUser.setGender(req.gender);
        repo.save(newUser);
        return new SignupResponse(true, null, "Registration successful", null, req.name, req.age, req.gender);
    }

    // 2. AUTHENTICATE EXISTING USER
    public SignupResponse authenticate(SigninRequest req) {
        Optional<User> userOpt = repo.findByMobile(req.mobile);
        if (userOpt.isEmpty()) {
            return new SignupResponse(false, "mobile", "Account not found", null, null, 0, null);
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

    // 3. GENERATE AND SEND OTP
    public boolean generateOtp(String mobile) {
        Optional<User> userOpt = repo.findByMobile(mobile);
        if (userOpt.isPresent()) {
            String otp = String.format("%06d", new Random().nextInt(999999));
            User user = userOpt.get();
            user.setOtp(otp);
            repo.save(user);
            try {
                String cleanMobile = mobile.replace("+91", "").replace("+", "").trim();
                String urlString = "https://www.fast2sms.com/dev/bulkV2?authorization=" + apiKey + 
                                   "&route=otp&variables_values=" + otp + "&numbers=" + cleanMobile;
                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                int responseCode = conn.getResponseCode();
                return responseCode == HttpURLConnection.HTTP_OK;
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    // 4. VERIFY OTP
    public boolean verifyOtp(String mobile, String otp) {
        return repo.findByMobile(mobile)
                .map(user -> user.getOtp() != null && user.getOtp().equals(otp))
                .orElse(false);
    }

    // 5. UPDATE PASSWORD
    public void updatePassword(String mobile, String newPassword) {
        repo.updatePassword(mobile, newPassword);
    }
}
