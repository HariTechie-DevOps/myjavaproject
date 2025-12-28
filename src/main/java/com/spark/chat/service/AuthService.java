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

    // PASTE YOUR FAST2SMS API KEY HERE
    private static final String API_KEY = "273o16HgyGabwheWFQT4JtkRplVdLiIXB5j0YPxAmMON8zvUrSNZG2BSCTRU9uj0npIb4dy8Mq3Ycz5h";

    private final UserRepository repo;

    public AuthService(UserRepository repo) {
        this.repo = repo;
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

    // --- FORGOT PASSWORD: STEP 1 (Send OTP to REAL PHONE) ---
    // FORGOT PASSWORD: STEP 1 (Generate, Save, and Send to Real Phone)
    public boolean generateOtp(String mobile) {
        Optional<User> userOpt = repo.findByMobile(mobile);
        if (userOpt.isPresent()) {
            // 1. Generate a clean 6-digit OTP
            String otp = String.format("%06d", new Random().nextInt(999999));

            // 2. Save to MySQL (Crucial so that verifyOtp can check it later)
            User user = userOpt.get();
             user.setOtp(otp);
            repo.save(user);

            // 3. SEND TO REAL PHONE VIA FAST2SMS
             try {
                // Fast2SMS requires 10-digit numbers (removes +91 or +)
                String cleanMobile = mobile.replace("+91", "").replace("+", "").trim();
            
                // Build the Fast2SMS URL (using the 'otp' route)
                String urlString = "https://www.fast2sms.com/dev/bulkV2?authorization=" + API_KEY + 
                               "&route=otp&variables_values=" + otp + 
                               "&numbers=" + cleanMobile;

                 URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("cache-control", "no-cache");
                 conn.setRequestProperty("User-Agent", "Mozilla/5.0");

                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) { // 200 Success
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = in.readLine()) != null) {
                        response.append(line);
                    }
                    in.close();

                    // Check if the Fast2SMS response actually says "request_id" or "true"
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
